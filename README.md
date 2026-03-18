# 🔍 Verificador de Códigos de Productos

Sistema web de verificación de códigos de productos desarrollado con **Spring Framework** y **Hibernate ORM**.

## 🚀 Características

- ✅ Verificación de códigos en tiempo real
- ✅ Integración con base de datos MySQL
- ✅ Arquitectura en capas (MVC)
- ✅ Inyección de dependencias con Spring
- ✅ ORM con Hibernate + JPA
- ✅ Connection pooling con HikariCP
- ✅ Transacciones gestionadas automáticamente
- ✅ Consulta de estudiantes vía API externa (pública y protegida)
- ✅ Proxy inverso integrado para evitar problemas de CORS

## 🛠️ Tecnologías Utilizadas

### Backend

- **Java 17** ☕
- **Spring Framework 6.1.3** 🍃
  - Spring Context
  - Spring ORM
  - Spring Transaction Management
- **Hibernate 6.4.2** 🐻
- **Jakarta Servlet 6.0**
- **Maven 3.9.11** 📦

### Base de Datos

- **MySQL 8.x** 🐬
- **HikariCP** (Connection Pool)

### Servidor

- **Apache Tomcat 11.0.13** 🐱

### Frontend

- **HTML5 + CSS3** 🎨
- **JSP (JavaServer Pages)** 📄
- **Fetch API** (consulta asíncrona a la API de estudiantes)

## 📋 Requisitos Previos

- JDK 17 o superior
- Apache Maven 3.9+
- MySQL 8.0+
- Apache Tomcat 11.0+

## 🔧 Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/TU_USUARIO/verificador-codigos-spring-hibernate.git
cd verificador-codigos-spring-hibernate
```

### 2. Configurar la base de datos

Ejecuta el script SQL en MySQL:

```bash
mysql -u root -p < database/productos.sql
```

### 3. Configurar credenciales

Edita `src/main/java/com/evidencia/config/AppConfig.java` y actualiza:

```java
config.setUsername("root");
config.setPassword("TU_CONTRASEÑA");
```

### 4. Compilar el proyecto

```bash
mvn clean package
```

### 5. Desplegar en Tomcat

Copia el archivo WAR generado:

```bash
cp target/verificador-codigos.war /ruta/a/tomcat/webapps/
```

### 6. Iniciar Tomcat

```bash
cd /ruta/a/tomcat/bin
./startup.sh  # Linux/Mac
startup.bat   # Windows
```

### 7. Acceder a la aplicación

Abre tu navegador en:

```
http://localhost:8089/verificador-codigos/
```

## 📁 Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   └── com/evidencia/
│   │       ├── config/
│   │       │   └── AppConfig.java          # Configuración de Spring
│   │       ├── model/
│   │       │   └── Producto.java           # Entidad JPA
│   │       ├── repository/
│   │       │   └── ProductoRepository.java # Capa de acceso a datos
│   │       ├── service/
│   │       │   └── ProductoService.java    # Lógica de negocio
│   │       └── servlets/
│   │           ├── CodigoServlet.java      # Controlador verificador de productos
│   │           └── ApiProxyServlet.java    # Proxy inverso hacia API de estudiantes
│   ├── resources/
│   └── webapp/
│       ├── WEB-INF/
│       │   └── web.xml                     # Configuración web
│       ├── index.html                      # Página principal del verificador
│       ├── estudiantes.html                # Consulta de estudiantes vía API
│       ├── resultado.jsp                   # Vista de resultados
│       └── estilos.css                     # Estilos
├── database/
│   └── productos.sql                       # Script de BD
└── pom.xml                                 # Configuración Maven
```

## 🏗️ Arquitectura

### Verificador de productos

```
┌─────────────────────┐
│   Frontend (JSP)    │
└──────────┬──────────┘
           │
           ↓
┌─────────────────────┐
│  Servlet Layer      │
│  (CodigoServlet)    │
└──────────┬──────────┘
           │ @Autowired
           ↓
┌─────────────────────┐
│  Service Layer      │
│  (ProductoService)  │
└──────────┬──────────┘
           │ @Autowired
           ↓
┌─────────────────────┐
│  Repository Layer   │
│ (ProductoRepository)│
└──────────┬──────────┘
           │ Hibernate/JPA
           ↓
┌─────────────────────┐
│   Database (MySQL)  │
└─────────────────────┘
```

### Consulta de estudiantes (API externa)

```
┌──────────────────────────┐
│  estudiantes.html        │
│  (Fetch API → /apiProxy) │
└────────────┬─────────────┘
             │ HTTP GET
             ↓
┌──────────────────────────┐
│  ApiProxyServlet         │  ← evita CORS, agrega cabeceras de auth
│  (/apiProxy)             │
└────────────┬─────────────┘
             │ HTTP GET (con X-API-KEY / X-USER-ID si modo=auth)
             ↓
┌──────────────────────────────────────────────────────────┐
│  API Externa - Escuela de Capacitación Petrolera (SENA)  │
│  Pública:   api.php                                      │
│  Protegida: api2.php                                     │
└──────────────────────────────────────────────────────────┘
```

## 🎓 Módulo de Consulta de Estudiantes

La página `estudiantes.html` permite consultar los estudiantes registrados en la API de la **Escuela de Capacitación Petrolera - SENA**.

### Modos de consulta

| Modo          | Endpoint   | Autenticación             |
| ------------- | ---------- | ------------------------- |
| **Pública**   | `api.php`  | Sin credenciales          |
| **Protegida** | `api2.php` | `X-API-KEY` + `X-USER-ID` |

### Cómo funciona el proxy

Para evitar errores de CORS al llamar a la API externa desde el navegador, se usa `ApiProxyServlet` como intermediario:

1. El frontend llama a `/apiProxy?mode=public` o `/apiProxy?mode=auth&id=3`
2. El servlet reenvía la petición a la API externa con las cabeceras necesarias
3. Devuelve la respuesta JSON al navegador

### Acceso

```
http://localhost:8089/verificador-codigos/estudiantes.html
```

## 🧪 Códigos de Prueba

- `PROD001` - Laptop Dell XPS 15 ✅
- `PROD002` - Mouse Logitech MX Master ✅
- `PROD003` - Teclado Mecánico Corsair ✅
- `PROD004` - Monitor Samsung 27" ✅
- `PROD005` - Auriculares Sony WH-1000XM4 ✅
- `PROD999` - Código no encontrado ❌

## 📝 Licencia

Este proyecto es de código abierto y está disponible bajo la [Licencia MIT](LICENSE).

## 👨‍💻 Autor

Jhon fernando y Nicolas Garzon - [GitHub](https://github.com/TU_USUARIO)

## 🙏 Agradecimientos

- Spring Framework
- Hibernate ORM
- Apache Tomcat
- MySQL Community

---

⭐ Si te gustó este proyecto, ¡dale una estrella en GitHub!
