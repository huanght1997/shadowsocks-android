/*******************************************************************************
 *                                                                             *
 *  Copyright (C) 2017 by Max Lv <max.c.lv@gmail.com>                          *
 *  Copyright (C) 2017 by Mygod Studio <contact-shadowsocks-android@mygod.be>  *
 *                                                                             *
 *  This program is free software: you can redistribute it and/or modify       *
 *  it under the terms of the GNU General Public License as published by       *
 *  the Free Software Foundation, either version 3 of the License, or          *
 *  (at your option) any later version.                                        *
 *                                                                             *
 *  This program is distributed in the hope that it will be useful,            *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of             *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the              *
 *  GNU General Public License for more details.                               *
 *                                                                             *
 *  You should have received a copy of the GNU General Public License          *
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.       *
 *                                                                             *
 *******************************************************************************/

package com.github.shadowsocks

import android.app.Activity
import android.content.Intent
import android.content.pm.ShortcutManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import com.github.shadowsocks.database.Profile
import com.github.shadowsocks.database.ProfileManager
import com.github.shadowsocks.utils.*
import com.google.zxing.*
import com.google.zxing.common.GlobalHistogramBinarizer
import com.google.zxing.qrcode.QRCodeReader

class ScannerActivity : AppCompatActivity(), ScannerFragment.Reader {
    companion object {
        private const val TAG = "ScannerActivity"
        private const val REQUEST_IMPORT = 2
        private const val REQUEST_IMPORT_OR_FINISH = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 25) getSystemService<ShortcutManager>()!!.reportShortcutUsed("scan")
        if (try {
                    getSystemService<CameraManager>()?.cameraIdList?.isEmpty()
                } catch (_: CameraAccessException) {
                    true
                } != false) {
            startImport()
            return
        }
        setContentView(R.layout.layout_scanner)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportFragmentManager.beginTransaction()
                .replace(R.id.barcode, ScannerFragment.newInstance(this))
                .commit()
    }

    override fun onQRCodeRead(text: String?) {
        Profile.findAllUrls(text, Core.currentProfile?.first).forEach { ProfileManager.createProfile(it) }
        onSupportNavigateUp()
    }

    override fun onCameraPermissionDenied() {
        Toast.makeText(this, R.string.add_profile_scanner_permission_required, Toast.LENGTH_SHORT).show()
        startImport()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.scanner_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        R.id.action_import_clipboard -> {
            startImport(true)
            true
        }
        else -> false
    }

    /**
     * See also: https://stackoverflow.com/a/31350642/2245107
     */
    override fun shouldUpRecreateTask(targetIntent: Intent?) = super.shouldUpRecreateTask(targetIntent) || isTaskRoot

    private fun startImport(manual: Boolean = false) = startActivityForResult(Intent(Intent.ACTION_GET_CONTENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "image/*"
        putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
    }, if (manual) REQUEST_IMPORT else REQUEST_IMPORT_OR_FINISH)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_IMPORT, REQUEST_IMPORT_OR_FINISH -> if (resultCode == Activity.RESULT_OK) {
                val feature = Core.currentProfile?.first
                var success = false
                try {
                    data!!.datas.forEachTry { uri ->
                        try {
                            val bitmap = contentResolver.openBitmap(uri)
                            val width = bitmap.width
                            val height = bitmap.height
                            val pixels = IntArray(width * height)
                            bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
                            val binaryBitmap = BinaryBitmap(GlobalHistogramBinarizer(RGBLuminanceSource(width, height, pixels)))

                            val hints = HashMap<DecodeHintType, Any>()
                            hints[DecodeHintType.CHARACTER_SET] = "utf-8"
                            hints[DecodeHintType.TRY_HARDER] = true
                            hints[DecodeHintType.POSSIBLE_FORMATS] = BarcodeFormat.QR_CODE

                            val result = QRCodeReader().decode(binaryBitmap, hints)
                            Profile.findAllUrls(result.text, feature).forEach {
                                ProfileManager.createProfile(it)
                                success = true
                            }
                        } catch (e: NotFoundException) {
                            // This is a normal exception when user chooses a wrong picture
                            // Don't let it bother me via Crashlystics.
                            e.printStackTrace()
                        }
                    }
                    Toast.makeText(this, if (success) R.string.action_import_msg else R.string.action_import_err,
                            Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this, e.readableMessage, Toast.LENGTH_LONG).show()
                }
                onSupportNavigateUp()
            } else if (requestCode == REQUEST_IMPORT_OR_FINISH) onSupportNavigateUp()
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
