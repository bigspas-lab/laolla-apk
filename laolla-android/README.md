# 📱 La Olla — APK Android

App de finanzas del hogar **La Olla** empaquetada como APK nativo para Android.

## ✅ Características del APK

- **localStorage habilitado** — los datos se guardan en el teléfono y persisten entre sesiones
- **Funciona sin internet** — app completamente offline
- **Pantalla completa** — sin barra de navegación de Chrome, experiencia nativa
- **Botón Atrás** de Android integrado
- **Diálogos nativos** — los `alert()`, `confirm()` y `prompt()` de JS usan componentes Android nativos
- **Orientación portrait** fija para experiencia óptima

---

## 🚀 Opción 1: Compilar con GitHub Actions (Recomendado, sin instalar nada)

1. **Subir este proyecto a GitHub:**
   ```
   git init
   git add .
   git commit -m "La Olla APK v1"
   git branch -M main
   git remote add origin https://github.com/TU_USUARIO/laolla-apk.git
   git push -u origin main
   ```

2. **El APK se compila automáticamente.** Ve a:
   - `GitHub repo → Actions → Build La Olla APK`
   - Haz clic en **"Run workflow"** si no inició solo

3. **Descargar el APK:**
   - En la acción completada → sección **Artifacts**
   - Descarga `LaOlla-debug-apk` (para pruebas)

4. **Instalar en Android:**
   - Transferir el `.apk` al teléfono
   - Habilitar **"Instalar apps de fuentes desconocidas"** en Ajustes → Seguridad
   - Abrir el archivo APK e instalar

---

## 🛠️ Opción 2: Compilar localmente con Android Studio

### Requisitos:
- Android Studio Hedgehog (2023.1.1) o superior
- JDK 17

### Pasos:
1. Abrir Android Studio → `File → Open` → seleccionar esta carpeta
2. Esperar que sincronice Gradle
3. `Build → Build Bundle(s) / APK(s) → Build APK(s)`
4. El APK queda en: `app/build/outputs/apk/debug/app-debug.apk`

---

## 🛠️ Opción 3: Compilar desde terminal (macOS/Linux)

### Requisitos:
- JDK 17: `brew install openjdk@17` (macOS) o `sudo apt install openjdk-17-jdk` (Linux)
- Android SDK: descargar desde https://developer.android.com/studio#command-tools

### Pasos:
```bash
# Configurar variable de entorno
export ANDROID_HOME=~/android-sdk
export PATH=$PATH:$ANDROID_HOME/platform-tools

# Compilar
./gradlew assembleDebug

# APK generado en:
# app/build/outputs/apk/debug/app-debug.apk
```

---

## 📦 Estructura del proyecto

```
laolla-android/
├── app/
│   ├── src/main/
│   │   ├── assets/
│   │   │   └── index.html          ← Tu app La Olla (completa)
│   │   ├── java/com/laolla/app/
│   │   │   └── MainActivity.java   ← WebView con localStorage
│   │   ├── res/
│   │   │   ├── layout/activity_main.xml
│   │   │   ├── values/strings.xml
│   │   │   ├── values/themes.xml
│   │   │   └── drawable*/          ← Íconos en todas las densidades
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── .github/workflows/
│   └── build-apk.yml               ← CI/CD para GitHub Actions
├── build.gradle
├── settings.gradle
└── README.md                       ← Este archivo
```

---

## 💾 Cómo funciona el almacenamiento

La app usa **localStorage del WebView de Android**, que:
- Se guarda en el almacenamiento interno del teléfono
- **Persiste aunque cierres la app**
- Se borra solo si desinstalar la app o limpiar datos manualmente
- Ruta física: `/data/data/com.laolla.app/app_webview/Default/Local Storage/`

---

## 🔄 Actualizar la app

Para actualizar con una nueva versión de `index.html`:
1. Reemplazar `app/src/main/assets/index.html`
2. Incrementar `versionCode` en `app/build.gradle` (ej: de `1` a `2`)
3. Recompilar el APK

---

## ⚙️ Configuración de densidades de pantalla

El APK incluye íconos para todas las densidades:
- mdpi (48px), hdpi (72px), xhdpi (96px), xxhdpi (144px), xxxhdpi (192px)

Compatible con Android 7.0 (API 24) en adelante.
