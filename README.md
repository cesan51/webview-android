# WebView Android Ajustable

**Plantilla de aplicación Android WebView lista para publicar en Google Play Store**

Esta es una plantilla de aplicación Android que convierte cualquier sitio web en una app nativa. Incluye funcionalidades esenciales como verificación de conexión, manejo de permisos y notificaciones.

**Ejemplo actual**: Configurada para [kaisof.com](https://kaisof.com)

---

## 🚀 Características

- 🌐 **WebView optimizado**: Convierte tu sitio web en app Android nativa
- 📶 **Monitor de conexión en tiempo real**: Detecta pérdida y recuperación de internet automáticamente
- 🔔 **Diálogos automáticos**: Notifica al usuario sobre el estado de la conexión
- 🎤 **Permiso de micrófono**: Maneja permisos tanto del sistema como del WebView
- 🔙 **Navegación con botón atrás**: Navega en el historial del WebView
- 🎨 **Iconos multiplataforma**: Compatible con Linux, Windows y navegadores
- 📱 **Lista para Play Store**: Configuración preparada para publicación en release

---

## ⚙️ Configuración para tu proyecto

### 1. Clonar el repositorio

```bash
git clone
```

### 2. Archivos que debes modificar

#### 📄 `AndroidManifest.xml`

Ubicación: `app/src/main/AndroidManifest.xml`

```xml
<!-- Cambiar el nombre de la aplicación -->
<application
    android:label="TU_NOMBRE_APP"
    ...>
    
<!-- Cambiar la URL de tu sitio web -->
<meta-data
    android:name="WEB_URL"
    android:value="https://tu-dominio.com" />
```

#### 📄 `build.gradle` (Module: app)

Ubicación: `app/build.gradle.kts`

```kotlin
android {
    namespace = "com.tudominio.webview"    // ⚠️ Cambiar por tu package namespace
    compileSdk = 36
    
    defaultConfig {
        applicationId = "com.tudominio.webview"  // ⚠️ Cambiar por tu package name único
        minSdk = 24                              // Android 7.0
        targetSdk = 36
        versionCode = 1                          // Incrementar con cada actualización
        versionName = "1.0"                      // Versión visible para usuarios
    }
    
    buildTypes {
        release {
            minifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}
```

#### 📄 `themes.xml`

Ubicación: `app/src/main/res/values/themes.xml`

```xml
<resources>
    <style name="Theme.TuNombreApp" parent="Theme.MaterialComponents.DayNight.NoActionBar">
        <!-- Personaliza los colores de tu app -->
        <item name="colorPrimary">@color/tu_color_primario</item>
        <item name="colorPrimaryVariant">@color/tu_color_variante</item>
        <item name="colorOnPrimary">@color/tu_color_texto</item>
    </style>
</resources>
```

#### 📄 `strings.xml`

Ubicación: `app/src/main/res/values/strings.xml`

```xml
<resources>
    <string name="app_name">Tu Nombre App</string>
    <string name="no_internet_title">Sin conexión</string>
    <string name="no_internet_message">Verifica tu conexión a internet</string>
</resources>
```

### 3. Cambiar los iconos de la app

1. Reemplaza los archivos en: `app/src/main/res/mipmap-*/`
2. Usa [Android Asset Studio](https://romannurik.github.io/AndroidAssetStudio/) para generar todos los tamaños
3. Formatos necesarios:
   - `ic_launcher.png` (icono redondo)
   - `ic_launcher_round.png` (icono adaptativo)
   - Tamaños: mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi

### 4. Configurar la URL de tu sitio web

Ubicación: `app/src/main/java/com/tudominio/webview/MainActivity.kt`

```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private val webUrl = "https://tu-dominio.com" // ⚠️ CAMBIAR ESTA URL
    
    // ... resto del código
}
```

**Importante**: 
- Cambia `com.kaisof.webview` por tu paquete en toda la estructura de carpetas
- La app monitorea la conexión en tiempo real y muestra diálogos automáticamente
- Maneja permisos de micrófono tanto del sistema como del WebView

---

## 🛠️ Compilación y publicación

### Compilar APK Release

```bash
./gradlew assembleRelease
```

El APK se genera en: `app/build/outputs/apk/release/`

### Generar App Bundle firmado para Play Store

1. En Android Studio: `Build > Generate Signed Bundle / APK`
2. Selecciona `Android App Bundle (.aab)`
3. Crea o selecciona tu keystore
4. Elige la variante `release`
5. El .aab se genera en: `app/build/outputs/bundle/release/`

### Subir a Google Play Store

1. Accede a [Google Play Console](https://play.google.com/console)
2. Crea una nueva aplicación
3. Sube el archivo `.aab` generado
4. Completa la información requerida (descripción, capturas, etc.)
5. Envía para revisión

---

## 📋 Requisitos del sistema

| Componente | Versión mínima |
|------------|----------------|
| Android Studio | 2022.1 o superior |
| minSdk | API 24 (Android 7.0) |
| targetSdk | API 36 |
| compileSdk | API 36 |
| Kotlin | 1.8+ |
| Gradle | 7.0+ |

---

## 🔒 Permisos utilizados

- 🌐 **INTERNET**: Para cargar el contenido web
- 📶 **ACCESS_NETWORK_STATE**: Para verificar conectividad
- 🎤 **RECORD_AUDIO**: Permiso de micrófono

---

## 📝 Checklist pre-publicación

- [ ] Cambiar `namespace` y `applicationId` en `build.gradle.kts`
- [ ] Cambiar package name `com.kaisof.webview` en toda la estructura
- [ ] Actualizar URL del sitio web en `MainActivity.kt`
- [ ] Actualizar nombre de la app en `strings.xml`
- [ ] Reemplazar todos los iconos de la app
- [ ] Personalizar colores en `themes.xml`
- [ ] Actualizar `AndroidManifest.xml` con tu información
- [ ] Probar en dispositivos físicos
- [ ] Generar keystore para firma
- [ ] Crear `.aab` en modo release
- [ ] Preparar capturas de pantalla para Play Store
- [ ] Redactar descripción y política de privacidad
- [ ] Revisar en Google Play Console requisitos actuales de API/SDK

---

## 🤝 Soporte y contribuciones

- **Issues**: Reporta problemas en la sección de issues
- **Pull Requests**: Las contribuciones son bienvenidas
- **Licencia**: MIT License - Libre para usar, modificar, reproducir y vender

---

## 📞 Contacto

Para consultas sobre esta plantilla:
- 🌐 Web: [kaisof.com](https://kaisof.com)
- 📧 Email: antonio.dev@kaisof.com

---

## ⚠️ Notas importantes

- **Lenguaje**: Desarrollada en Kotlin para Android Studio
- **Funcionalidad**: WebView con monitoreo de conexión en tiempo real y permiso de micrófono
- **Build Type**: Las apps se generan en modo **release** para Play Store
- Cambia el package name `com.kaisof.webview` en toda la estructura de carpetas
- Verifica que tu sitio web sea responsive y funcione bien en móviles
- Implementa una política de privacidad si recopilas datos de usuarios
- **Importante**: Revisa regularmente en Google Play Console si necesitas actualizar el nivel de API o SDK de la app

---

**Versión actual del template**: 1.0  
**Última actualización**: Noviembre 2025