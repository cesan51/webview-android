package com.kaisof.webview

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Network
import android.net.NetworkRequest
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.webkit.ConsoleMessage
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private val webUrl = "https://kaisof.com"
    private lateinit var connectivityManager: ConnectivityManager
    private var isConnected = true
    private var noInternetDialog: AlertDialog? = null

    companion object {
        private const val AUDIO_PERMISSION_CODE = 1
        private const val TAG = "MainActivity"
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            runOnUiThread {
                if (!isConnected) {
                    isConnected = true
                    Toast.makeText(this@MainActivity, "Connection restored", Toast.LENGTH_SHORT).show()
                    noInternetDialog?.dismiss()
                    loadWebsite()
                }
            }
        }

        override fun onLost(network: Network) {
            runOnUiThread {
                isConnected = false
                showNoInternetDialog()
                Toast.makeText(this@MainActivity, "Connection lost", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        webView = findViewById(R.id.webView)
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Solicitar permiso de micrófono en tiempo de ejecución
        checkAudioPermission()

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        setupWebView()

        if (isNetworkAvailable()) {
            loadWebsite()
        } else {
            showNoInternetDialog()
        }
    }

    private fun setupWebView() {
        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true
        settings.mediaPlaybackRequiresUserGesture = false

        // Configuraciones adicionales para micrófono
        settings.databaseEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.allowFileAccess = true
        settings.allowContentAccess = true

        // WebChromeClient mejorado para manejar permisos de micrófono
        webView.webChromeClient = object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest?) {
                runOnUiThread {
                    if (request != null) {
                        val requestedResources = request.resources
                        Log.d(TAG, "Permisos solicitados: ${requestedResources.joinToString()}")

                        for (resource in requestedResources) {
                            if (resource == PermissionRequest.RESOURCE_AUDIO_CAPTURE) {
                                // Verificar si ya tenemos el permiso del sistema
                                if (ContextCompat.checkSelfPermission(
                                        this@MainActivity,
                                        Manifest.permission.RECORD_AUDIO
                                    ) == PackageManager.PERMISSION_GRANTED
                                ) {
                                    Log.d(TAG, "Concediendo permiso de audio al WebView")
                                    request.grant(arrayOf(PermissionRequest.RESOURCE_AUDIO_CAPTURE))
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Micrófono activado",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Log.d(TAG, "Permiso de audio no concedido, solicitando...")
                                    request.deny()
                                    checkAudioPermission()
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Por favor, concede permiso de micrófono",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                return@runOnUiThread
                            }
                        }
                        // Si no es audio, conceder otros permisos
                        request.grant(requestedResources)
                    }
                }
            }

            // Mostrar mensajes de consola de la web (útil para debugging)
            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                consoleMessage?.let {
                    Log.d(TAG, "${it.message()} -- Línea ${it.lineNumber()} de ${it.sourceId()}")
                }
                return true
            }
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                runOnUiThread {
                    Log.e(TAG, "Error cargando página: $description")
                    if (!isNetworkAvailable()) {
                        showNoInternetDialog()
                    }
                }
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.d(TAG, "Página cargada: $url")
            }
        }
    }

    private fun loadWebsite() {
        if (isNetworkAvailable()) {
            Log.d(TAG, "Cargando URL: $webUrl")
            webView.loadUrl(webUrl)
        } else {
            showNoInternetDialog()
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }

    private fun showNoInternetDialog() {
        noInternetDialog?.dismiss()
        noInternetDialog = AlertDialog.Builder(this)
            .setTitle("Sin Conexión")
            .setMessage("No hay conexión a internet. Por favor, verifica tu conexión e intenta de nuevo.")
            .setPositiveButton("Reintentar") { dialog, _ ->
                dialog.dismiss()
                if (isNetworkAvailable()) {
                    loadWebsite()
                } else {
                    showNoInternetDialog()
                }
            }
            .setNegativeButton("Salir") { _, _ -> finish() }
            .setCancelable(false)
            .show()
    }

    private fun checkAudioPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "Solicitando permiso de audio")
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                AUDIO_PERMISSION_CODE
            )
        } else {
            Log.d(TAG, "Permiso de audio ya concedido")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            AUDIO_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permiso de micrófono concedido por el usuario")
                    Toast.makeText(
                        this,
                        "Permiso de micrófono concedido. Recargando página...",
                        Toast.LENGTH_LONG
                    ).show()
                    // Recargar la página para que tome el nuevo permiso
                    loadWebsite()
                } else {
                    Log.d(TAG, "Permiso de micrófono denegado por el usuario")
                    Toast.makeText(
                        this,
                        "La app necesita acceso al micrófono para funcionar correctamente",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterNetworkCallback(networkCallback)
        noInternetDialog?.dismiss()
    }
}