# 🌟 Proyecto Android – Sistema de Fidelización para Cafetería (SweetScape App)

Aplicación móvil Android desarrollada en **Java**, que implementa un sistema completo de fidelización para una cafetería.  
Incluye **CRUDs**, gestión de **tiendas con Google Maps**, **Firebase**, **escáner QR**, **foro**, **beneficios**, **canjes**, y **persistencia local con Room**.

Su diseño modular permite una administración completa mediante múltiples *Fragments* y navegación fluida usando *Navigation Components*.

---

## 🚀 Características Principales

### 🛠️ CRUDs Completos
Cada entidad del sistema cuenta con operaciones:
- Crear  
- Leer  
- Actualizar  
- Eliminar  
- Visualización con RecyclerView  
- Selección de ítems  
- Validación de datos  
- Persistencia en **Room Database**

Entidades incluidas:
- Reglas  
- Clientes  
- Productos  
- Tiendas  
- Beneficios  
- Visitas  
- Canjes  

---

## 🗺️ Gestión de Tiendas con Google Maps
- Selección de ubicación en mapa interactivo  
- Guardado de coordenadas en Room (double)  
- Conversión a float para compatibilidad con el fragment  
- Visualización de ubicación guardada  
- Modo vista y modo selección  
- Implementado con Google Maps SDK  

---

## 🔥 Integración con Firebase
Sincronización opcional de datos usando Realtime Database:
- Guardar tiendas
- Actualizar datos
- Eliminar ubicaciones

Arquitectura basada en **Repositorios Firebase + Controladores**.

---

## 📸 Escaneo de Códigos QR
Sistema QR integrado:
- Escaneo desde *ScanQRFragment*
- Registro de visitas o puntos
- Integración con módulos de fidelización

---

## 💬 Foro de Usuarios
Una sección social donde los usuarios pueden:
- Publicar mensajes o contenido
- Interactuar con otros usuarios
- Ser moderados desde el panel admin

Pensado para aumentar la fidelización.

---

## 🏆 Sistema de Beneficios
- CRUD completo de beneficios  
- Asignación y canje de recompensas  
- Registro automático del historial  
- Relación con visitas y puntos  

---

## 📱 Menú Administrativo Completo
Desde el menú principal se puede administrar:
- Reglas  
- Productos  
- Tiendas  
- Clientes  
- Beneficios  
- Canjes  
- Visitas  
- Foro  
- QR  

Cada módulo implementado con su respectivo Fragment.

---

## 🧱 Tecnologías Utilizadas

### 👨‍💻 Lenguaje
- Java (Android SDK)

### 🗄️ Base de Datos
- Room Database  
- DAOs, Entities y TypeConverters  
- Persistencia en SQLite  

### 🎨 UI / UX
- Fragments  
- RecyclerView con ViewHolder  
- ConstraintLayout  
- Material Design  

### ☁️ Servicios
- Firebase Realtime Database  
- Google Maps SDK  

### ⚙️ Arquitectura
- Estructura MVC/MVVM ligera  
- Controladores para encapsular lógica  
- Comunicación entre fragments con FragmentResultListener  
- Navigation Component + NavGraph  

---

## 📦 Instalación

1️⃣ Clonar el repositorio:
```bash
git clone https://github.com/BrianG-R/Proyecto-Android.git

