package com.laolla.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends Activity {

    private WebView webView;

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Pantalla completa sin status bar
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        // Mantener pantalla encendida mientras la app está activa
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webview);

        configureWebView();

        // Cargar la app desde assets (almacenamiento local del APK)
        webView.loadUrl("file:///android_asset/index.html");
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void configureWebView() {
        WebSettings settings = webView.getSettings();

        // JavaScript habilitado (requerido por La Olla)
        settings.setJavaScriptEnabled(true);

        // *** CLAVE: Habilitar almacenamiento local (localStorage) ***
        settings.setDomStorageEnabled(true);           // localStorage / sessionStorage
        settings.setDatabaseEnabled(true);             // Web SQL Database (legacy)
        settings.setAllowFileAccess(true);             // acceso a archivos locales
        settings.setAllowContentAccess(true);

        // Ruta de almacenamiento de base de datos
        String dbPath = getApplicationContext().getDir("webdb", MODE_PRIVATE).getPath();
        settings.setDatabasePath(dbPath);

        // Caché para funcionamiento offline
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAppCacheEnabled(true);
        String cachePath = getCacheDir().getAbsolutePath();
        settings.setAppCachePath(cachePath);

        // Escala y zoom
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(false);

        // Mejoras de rendimiento
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        // Fuentes y texto
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setMinimumFontSize(12);

        // Acceso a contenido mixto (necesario para assets locales)
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        // WebViewClient: manejar navegación interna
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Ocultar el splash/loading si existiera
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                    String description, String failingUrl) {
                // Silenciar errores menores de assets
            }
        });

        // WebChromeClient: habilitar alert/confirm/prompt nativos de JS
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url,
                    String message, android.webkit.JsResult result) {
                // Usar diálogos nativos de Android en lugar de JS alerts
                new android.app.AlertDialog.Builder(MainActivity.this)
                    .setMessage(message)
                    .setPositiveButton("OK", (d, w) -> result.confirm())
                    .setOnCancelListener(d -> result.cancel())
                    .create()
                    .show();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url,
                    String message, android.webkit.JsResult result) {
                new android.app.AlertDialog.Builder(MainActivity.this)
                    .setMessage(message)
                    .setPositiveButton("Sí", (d, w) -> result.confirm())
                    .setNegativeButton("No", (d, w) -> result.cancel())
                    .setOnCancelListener(d -> result.cancel())
                    .create()
                    .show();
                return true;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message,
                    String defaultValue, android.webkit.JsPromptResult result) {
                // Para prompts como "¿Cuánto querés abonar?"
                final android.widget.EditText input = new android.widget.EditText(MainActivity.this);
                input.setText(defaultValue != null ? defaultValue : "");
                input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER
                    | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);

                new android.app.AlertDialog.Builder(MainActivity.this)
                    .setMessage(message)
                    .setView(input)
                    .setPositiveButton("OK", (d, w) -> result.confirm(input.getText().toString()))
                    .setNegativeButton("Cancelar", (d, w) -> result.cancel())
                    .setOnCancelListener(d -> result.cancel())
                    .create()
                    .show();
                return true;
            }
        });
    }

    // Botón "Atrás" navega dentro de la WebView
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            // Confirmar salida
            new android.app.AlertDialog.Builder(this)
                .setMessage("¿Salir de La Olla?")
                .setPositiveButton("Salir", (d, w) -> super.onBackPressed())
                .setNegativeButton("Cancelar", null)
                .show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
        // Guardar estado al pausar
        webView.evaluateJavascript(
            "if(typeof saveDB==='function'){console.log('DB guardada al pausar');}",
            null
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }
}
