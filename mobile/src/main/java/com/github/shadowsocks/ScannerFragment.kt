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