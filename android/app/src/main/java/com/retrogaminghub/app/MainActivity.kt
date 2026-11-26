package com.retrogaminghub.app

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.view.WindowManager
import android.graphics.Color
import android.view.ViewGroup
import android.widget.FrameLayout
import android.webkit.JavascriptInterface
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.webkit.WebViewAssetLoader
import androidx.core.view.WindowCompat

class MainActivity : Activity() {
    private lateinit var webView: WebView
    private lateinit var root: FrameLayout
    private var fullscreenView: View? = null
    private var fullscreenCallback: WebChromeClient.CustomViewCallback? = null
    private var fileChooserCallback: ValueCallback<Array<Uri>>? = null
    private var pendingDownload: ByteArray? = null
    private var pendingMime: String = "application/octet-stream"
    private var pendingName: String = "game-save.sav"

    companion object {
        private const val PICK_FILE = 4101
        private const val SAVE_FILE = 4102
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.statusBarColor = Color.rgb(9, 11, 18)
        window.navigationBarColor = Color.rgb(9, 11, 18)
        // Explicitly fit the home WebView inside system bars, including Android 15
        // edge-to-edge enforcement, so camera cutouts never cover page controls.
        WindowCompat.setDecorFitsSystemWindows(window, true)
        window.decorView.systemUiVisibility = 0

        val loader = WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(this))
            .build()

        root = FrameLayout(this)
        webView = WebView(this)
        root.addView(webView, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        setContentView(root)
        webView.settings.javaScriptEnabled = true
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = false
        webView.settings.textZoom = 100
        webView.settings.domStorageEnabled = true
        webView.settings.databaseEnabled = true
        webView.settings.allowFileAccess = false
        webView.settings.allowContentAccess = true
        webView.settings.mediaPlaybackRequiresUserGesture = false
        webView.settings.javaScriptCanOpenWindowsAutomatically = false
        webView.addJavascriptInterface(NativeBridge(), "AndroidBridge")
        webView.webViewClient = object : WebViewClient() {
            override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? =
                loader.shouldInterceptRequest(request.url) ?: super.shouldInterceptRequest(view, request)
        }
        webView.webChromeClient = object : WebChromeClient() {
            override fun onShowCustomView(view: View, callback: CustomViewCallback) {
                if (fullscreenView != null) { callback.onCustomViewHidden(); return }
                fullscreenView = view
                fullscreenCallback = callback
                webView.visibility = View.GONE
                root.addView(view, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                WindowCompat.setDecorFitsSystemWindows(window, false)
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            }
            override fun onHideCustomView() {
                fullscreenView?.let { root.removeView(it) }
                fullscreenView = null
                fullscreenCallback?.onCustomViewHidden()
                fullscreenCallback = null
                webView.visibility = View.VISIBLE
                WindowCompat.setDecorFitsSystemWindows(window, true)
                window.decorView.systemUiVisibility = 0
            }
            override fun onShowFileChooser(view: WebView, callback: ValueCallback<Array<Uri>>, params: FileChooserParams): Boolean {
                fileChooserCallback?.onReceiveValue(null)
                fileChooserCallback = callback
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "*/*"
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("application/octet-stream", "application/zip", "application/x-gba-rom", "application/x-save"))
                }
                startActivityForResult(intent, PICK_FILE)
                return true
            }
        }
        webView.loadUrl("https://appassets.androidplatform.net/assets/index.html")


    }

    @Deprecated("Use system back dispatcher")
    override fun onBackPressed() {
        if (fullscreenView != null) { webView.evaluateJavascript("document.exitFullscreen()", null); return }
        webView.evaluateJavascript("if(document.fullscreenElement){document.exitFullscreen()}else if(document.getElementById('playerOverlay')?.classList.contains('open')){document.getElementById('closePlayer')?.click()}else{true}", null)
    }

    inner class NativeBridge {
        @JavascriptInterface
        fun saveFile(base64: String, filename: String, mime: String) {
            pendingDownload = Base64.decode(base64, Base64.DEFAULT)
            pendingName = filename.replace(Regex("[^A-Za-z0-9._-]"), "_").take(100).ifBlank { "game-save.sav" }
            pendingMime = mime.ifBlank { "application/octet-stream" }
            runOnUiThread {
                val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = pendingMime
                    putExtra(Intent.EXTRA_TITLE, pendingName)
                }
                startActivityForResult(intent, SAVE_FILE)
            }
        }
    }

    @Deprecated("Activity result API kept compact for broad Android Studio compatibility")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FILE) {
            val result = if (resultCode == RESULT_OK && data?.data != null) arrayOf(data.data!!) else null
            fileChooserCallback?.onReceiveValue(result)
            fileChooserCallback = null
        } else if (requestCode == SAVE_FILE) {
            if (resultCode == RESULT_OK && data?.data != null && pendingDownload != null) {
                contentResolver.openOutputStream(data.data!!)?.use { it.write(pendingDownload!!) }
            }
            pendingDownload = null
        }
    }

    override fun onDestroy() {
        webView.stopLoading()
        webView.destroy()
        super.onDestroy()
    }
}
