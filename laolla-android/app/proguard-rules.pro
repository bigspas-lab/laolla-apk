# Reglas ProGuard para La Olla
-keep class com.laolla.app.** { *; }
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
