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
│   │           └── CodigoServlet.java      # Controlador web
│   ├── resources/
│   └── webapp/
│       ├── WEB-INF/
│       │   └── web.xml                     # Configuración web
│       ├── index.html                      # Página principal
│       ├── resultado.jsp                   # Vista de resultados
│       └── estilos.css                     # Estilos
├── database/
│   └── productos.sql                       # Script de BD
└── pom.xml                                 # Configuración Maven
```

## 🏗️ Arquitectura
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
```

---

## 📊 ESTRUCTURA FINAL DEL REPOSITORIO
```
verificador-codigos-spring-hibernate/
├── .gitignore
├── README.md
├── pom.xml
├── database/
│   └── productos.sql
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/evidencia/
    │   │       ├── config/
    │   │       ├── model/
    │   │       ├── repository/
    │   │       ├── service/
    │   │       └── servlets/
    │   └── webapp/
    │       ├── WEB-INF/
    │       ├── index.html
    │       ├── resultado.jsp
    │       └── estilos.css
    └── test/