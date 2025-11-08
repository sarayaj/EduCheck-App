# EduCheck

EduCheck es una aplicación móvil desarrollada en **Kotlin** utilizando **Jetpack Compose**, **Room Database** y la arquitectura **MVVM**, orientada a la gestión de asistencia de docentes dentro de un entorno académico.

---

## Descripción General

El proyecto fue creado como parte de la asignatura **DSY1105 – Desarrollo de Aplicaciones Móviles**, perteneciente a la carrera **Ingeniería en Informática (Duoc UC)**.  
Su propósito es ofrecer una interfaz funcional, moderna y fácil de usar que permita:

- Registrar nuevos docentes  
- Iniciar sesión con validación de datos  
- Visualizar y gestionar registros de asistencia  
- Mantener la información mediante **persistencia local (Room)**  

---

## Tecnologías Utilizadas

- **Kotlin** – Lenguaje principal  
- **Jetpack Compose** – Interfaz de usuario declarativa  
- **Material Design 3** – Estilo visual  
- **Room Database** – Persistencia local  
- **MVVM** – Separación lógica y mantenibilidad  
- **Coroutines / Flow** – Procesos asincrónicos  
- **Android Studio** – IDE de desarrollo  
- **Git & GitHub** – Control de versiones  

---

## Cómo Ejecutar el Proyecto

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/sarayaj/EduCheck.git
   ```

2. Abrir el proyecto en **Android Studio** (versión Giraffe o superior)  
3. Esperar la sincronización de dependencias de **Gradle**  
4. Ejecutar el proyecto en un **emulador o dispositivo físico (API 34 recomendada)**  
5. Probar las pantallas de **Login**, **Registro**, **Home** y **Asistencia**

---

## Arquitectura del Proyecto

El proyecto sigue el patrón **MVVM**, separando responsabilidades de manera clara:

```
com.sebastianaraya.educheck
 ┣ data
 ┃ ┣ local → Entities, DAO, Database
 ┃ ┗ repository → Lógica de acceso a datos
 ┣ ui
 ┃ ┣ screens → Pantallas principales (Login, Register, Home, Attendance)
 ┃ ┗ theme → Estilos visuales y colores
 ┣ navigation → Control de rutas (NavHost)
 ┣ viewmodel → Lógica de negocio y estado
 ┗ MainActivity.kt → Punto de entrada de la aplicación
```

---

## Funcionalidades Principales

- Registro e inicio de sesión de docentes  
- Validaciones visuales en formularios  
- Persistencia de datos local con Room  
- Interfaz moderna con animaciones y Material 3  
- Navegación fluida entre pantallas  
- Diseño modular y mantenible  

---

## Autor

**Sebastián Alejandro Araya Jara**  
Estudiante de **Ingeniería en Informática – Duoc UC**  





