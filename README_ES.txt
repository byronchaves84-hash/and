MOORTY VOICE ANDROID — PROYECTO BASE NATIVO
===========================================

Este es un proyecto Android Studio nativo en Kotlin. Incluye:
- Interfaz oscura roja/negra.
- Selector TikTok LIVE / YouTube LIVE.
- Selector @moorty_yt / @tocolita para TikTok.
- Casillas de eventos que se desean anunciar.
- Prueba de voz en español usando el motor TextToSpeech de Android.
- Servicio de voz en primer plano con notificación.

LIMITACIONES IMPORTANTES
------------------------
Esta es la base nativa de Android, NO una versión completa de detección de eventos LIVE todavía. Los selectores y casillas configuran la interfaz, pero la aplicación aún no se conecta a los eventos reales de TikTok ni de YouTube. Tampoco envía audio a OBS todavía.

Para completar esas funciones hay que integrar y probar por separado:
1. TikTok LIVE: conexión no oficial compatible con Android y manejo de desconexiones/eventos.
2. YouTube LIVE: API Data/Live Chat, credenciales y autorización seguras.
3. OBS: transporte de audio a la PC (por ejemplo, un receptor compañero en Windows en la misma red) o una solución de audio compatible.

No introduzcas ni compartas claves API en capturas o chats. Para generar APK:
1. Instala Android Studio en Windows.
2. Abre la carpeta MoortyVoiceAndroid.
3. Deja que Gradle sincronice el proyecto.
4. Usa Build > Build APK(s).

Requisitos del proyecto: Android Studio reciente, JDK 17 y conexión a internet durante la primera sincronización de Gradle.
