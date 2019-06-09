/*******************************************************************************
 *                                                                             *
 *  Copyright (C) 2017 by Max Lv <max.c.lv@gmail.com>                          *
 *  Copyright (C) 2017 by Mygod Studio <contact-shadowsocks-android@mygod.be>  *
 *  Copyright (C) 2019 by Haitao Huang <hht970222@gmail.com>                   *
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

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.dlazaro66.qrcodereaderview.QRCodeReaderView

class ScannerFragment : Fragment(), QRCodeReaderView.OnQRCodeReadListener {
    private lateinit var decoderView: QRCodeReaderView
    private var reader: Reader? = null
    private var firstRead = false

    companion object {
        private const val REQUEST_CODE_PERMISSION_CAMERA = 1

        @JvmStatic
        fun newInstance(reader: Reader): ScannerFragment {
            val fragment = ScannerFragment()
            fragment.setReader(reader)
            return fragment
        }
    }

    fun setReader(reader: Reader) {
        this.reader = reader
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.layout_fragment_scanner, container, false)
        decoderView = rootView.findViewById(R.id.decoder)
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CODE_PERMISSION_CAMERA)
        } else {
            initDecoderView()
        }
        return rootView
    }

    private fun initDecoderView() {
        decoderView.setOnQRCodeReadListener(this)
        decoderView.setQRDecodingEnabled(true)
        decoderView.setAutofocusInterval(2000L)
        decoderView.setTorchEnabled(true)
        decoderView.setBackCamera()
    }

    override fun onQRCodeRead(text: String?, points: Array<out PointF>?) {
        // This method loves to return many times.
        // Once we read one, filter other onQRCodeRead
        // Thanks god it's single thread.
        if (firstRead) return
        firstRead = true
        reader?.onQRCodeRead(text)
    }

    override fun onResume() {
        super.onResume()
        firstRead = false
        decoderView.startCamera()
    }

    override fun onPause() {
        super.onPause()
        decoderView.stopCamera()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_PERMISSION_CAMERA ->
                if (grantResults.size == 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initDecoderView()
                    // https://github.com/dlazaro66/QRCodeReaderView/issues/171
                    decoderView.visibility = View.GONE
                    decoderView.visibility = View.VISIBLE
                } else {
                    reader?.onCameraPermissionDenied()
                }
        }
    }

    interface Reader {
        fun onQRCodeRead(text: String?)
        fun onCameraPermissionDenied()
    }
}