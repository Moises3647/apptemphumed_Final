# AeroStat - Monitoreo de Temperatura y Humedad

AeroStat es una aplicación Android moderna diseñada para el monitoreo periódico de condiciones ambientales (temperatura y humedad). La aplicación consume una API REST para mostrar las mediciones registradas cada hora, permitiendo un análisis de variaciones y un historial detallado.

## 🚀 Características

- **Dashboard Principal**: Visualización del último estado registrado por el sensor (actualizado cada hora).
- **Detalle de Temperatura**: Gráficos o listas con la evolución de la temperatura y mensajes de análisis.
- **Detalle de Humedad**: Seguimiento específico de los niveles de humedad.
- **Historial**: Consulta de las últimas mediciones registradas por el sensor para un seguimiento histórico.
- **Análisis Inteligente**: Mensajes informativos sobre las variaciones detectadas entre cada registro.

## 🛠️ Stack Tecnológico

- **Lenguaje**: [Kotlin](https://kotlinlang.org/)
- **Interfaz de Usuario**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Arquitectura**: MVVM (Model-View-ViewModel)
- **Networking**: [Retrofit 2](https://square.github.io/retrofit/) & Gson
- **Navegación**: Navigation Compose
- **Componentes**: Material Design 3

## 📦 Instalación y Configuración

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/tu-usuario/apptemphumed.git
   ```
2. **Abrir en Android Studio:**
   Importa el proyecto como un proyecto Gradle existente.
3. **Configuración de la API:**
   Asegúrate de configurar la URL base de la API en el servicio de red. (Ubicación: `app/src/main/java/gonzalez/moises/apptemphumed/data/network/`)
4. **Ejecutar:**
   Selecciona un emulador o dispositivo físico con Android 10 (API 29) o superior.

## 📄 Requisitos del Sistema

Consulta el archivo [REQUIREMENTS.md](REQUIREMENTS.md) para ver los detalles técnicos y dependencias necesarias.

## ✒️ Autor

* **Moisés González** 
* **José Miguel Parra Díaz** 
