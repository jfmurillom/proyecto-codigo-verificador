# 🔍 Verificador de Códigos de Productos

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.9+-blue.svg)](https://maven.apache.org/)
[![Tomcat](https://img.shields.io/badge/Tomcat-11.0-yellow.svg)](https://tomcat.apache.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

Aplicación web desarrollada en **Java** que permite verificar la existencia de códigos de productos en una base de datos MySQL mediante una interfaz web moderna y responsiva.

![Screenshot](docs/screenshot.png)

## 📋 Tabla de Contenidos

- [Características](#-características)
- [Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [Uso](#-uso)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Deployment](#-deployment)
- [Contribuir](#-contribuir)
- [Licencia](#-licencia)
- [Autores](#-autores)

## ✨ Características

- ✅ **Validación en tiempo real** de códigos de productos
- 🎨 **Interfaz moderna** con CSS3 y animaciones
- 📱 **Diseño responsivo** compatible con móviles
- 🔒 **Consultas seguras** mediante PreparedStatement (protección contra SQL Injection)
- ⚡ **Búsqueda rápida** en base de datos MySQL
- 🎯 **Feedback visual** con estados de éxito y error
- 📊 **Arquitectura MVC** bien estructurada

## 🛠️ Tecnologías Utilizadas

### Backend

- **Java 17** - Lenguaje de programación
- **Jakarta EE (Servlets & JSP)** - Framework web
- **JDBC** - Conexión a base de datos
- **Maven** - Gestión de dependencias y build

### Frontend

- **HTML5** - Estructura
- **CSS3** - Estilos y animaciones
- **JavaScript** - Validaciones del lado del cliente

### Base de Datos

- **MySQL 8.0** - Sistema de gestión de base de datos

### Servidor

- **Apache Tomcat 11.0** - Servidor de aplicaciones

## 📦 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

- ☕ **JDK 17 o superior**

  ```bash
  java -version
  ```

- 📦 **Apache Maven 3.9+**

  ```bash
  mvn -version
  ```

- 🗄️ **MySQL Server 8.0+**

  ```bash
  mysql --version
  ```

- 🚀 **Apache Tomcat 11.0+**

  - Descarga: https://tomcat.apache.org/download-11.cgi

- 💻 **Visual Studio Code** (Recomendado)
  - Extensiones: Java Extension Pack, Tomcat for Java

## 🚀 Instalación

### 1. Clonar el Repositorio

```bash
git clone https://github.com/tu-usuario/proyecto-codigo-verificador.git
cd proyecto-codigo-verificador
```

### 2. Configurar la Base de Datos

**Iniciar MySQL:**

```bash
# Windows
net start MySQL80

# Linux/Mac
sudo systemctl start mysql
```

**Crear la base de datos:**

```bash
mysql -u root -p < database/productos.sql
```

O ejecutar el script directamente en MySQL Workbench:

- Abrir MySQL Workbench
- Conectar a `localhost`
- Ejecutar el archivo `database/productos.sql`

**Verificar:**

```sql
USE verificador_codigos;
SELECT * FROM productos;
```

Deberías ver 5 productos de ejemplo.

### 3. Configurar Credenciales de Base de Datos

Editar el archivo `src/main/java/com/evidencia/servlets/CodigoServlet.java`:

```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/verificador_codigos";
private static final String DB_USER = "root"; // Tu usuario de MySQL
private static final String DB_PASSWORD = ""; // Tu contraseña de MySQL
```

⚠️ **Importante:** No subas credenciales reales a GitHub. En producción, usa variables de entorno.

### 4. Compilar el Proyecto

```bash
mvn clean package
```

Esto generará el archivo `target/verificador-codigos.war`

### 5. Desplegar en Tomcat

**Opción A: Copia manual**

```bash
# Windows
copy target\verificador-codigos.war "C:\ruta\a\tomcat\webapps\"

# Linux/Mac
cp target/verificador-codigos.war /path/to/tomcat/webapps/
```

**Opción B: Desde VS Code**

- Panel "TOMCAT SERVERS"
- Click derecho en el servidor
- "Add Deployment" → Seleccionar el WAR

### 6. Iniciar Tomcat

**Windows:**

```batch
cd C:\ruta\a\tomcat\bin
startup.bat
```

**Linux/Mac:**

```bash
cd /path/to/tomcat/bin
./startup.sh
```

### 7. Acceder a la Aplicación

Abrir en el navegador:

```
http://localhost:8080/verificador-codigos/
```

Si Tomcat usa otro puerto (ej: 8089):

```
http://localhost:8089/verificador-codigos/
```

## ⚙️ Configuración

### Variables de Entorno (Producción)

Para evitar hardcodear credenciales:

```bash
# Linux/Mac
export DB_USER="root"
export DB_PASSWORD="mi_password"

# Windows
set DB_USER=root
set DB_PASSWORD=mi_password
```

Modificar el código para leer de variables de entorno:

```java
private static final String DB_USER = System.getenv("DB_USER");
private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");
```

### Cambiar Puerto de Tomcat

Editar `tomcat/conf/server.xml`:

```xml
<Connector port="8080" protocol="HTTP/1.1" ... />
```

## 📖 Uso

### Interfaz de Usuario

1. **Ingresar código** en el campo de texto
2. **Hacer click** en "Verificar Código"
3. **Ver resultado:**
   - ✅ Verde = Código encontrado (muestra nombre del producto)
   - ❌ Rojo = Código no encontrado

### Códigos de Prueba

- `PROD001` - Laptop Dell XPS 15
- `PROD002` - Mouse Logitech MX Master
- `PROD003` - Teclado Mecánico Corsair
- `PROD004` - Monitor Samsung 27"
- `PROD005` - Auriculares Sony WH-1000XM4

### API Endpoints

| Método | Endpoint           | Descripción       |
| ------ | ------------------ | ----------------- |
| GET    | `/index.html`      | Página principal  |
| POST   | `/verificarCodigo` | Verificar código  |
| GET    | `/resultado.jsp`   | Mostrar resultado |

## 📁 Estructura del Proyecto

```
proyecto-codigo-verificador/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── evidencia/
│       │           └── servlets/
│       │               └── CodigoServlet.java    # Servlet principal
│       │
│       └── webapp/
│           ├── WEB-INF/
│           │   └── web.xml                       # Configuración web
│           ├── index.html                        # Página principal
│           ├── resultado.jsp                     # Página de resultados
│           └── estilos.css                       # Estilos CSS
│
├── database/
│   └── productos.sql                             # Script de BD
│
├── target/                                       # Archivos compilados (ignorado en Git)
│   └── verificador-codigos.war                   # Archivo desplegable
│
├── .gitignore                                    # Archivos ignorados por Git
├── pom.xml                                       # Configuración Maven
└── README.md                                     # Este archivo
```

## 🚀 Deployment

### Desarrollo Local

```bash
mvn clean package
# Copiar WAR a Tomcat
# Acceder a localhost:8080/verificador-codigos/
```

### Producción

1. **Configurar base de datos en servidor**
2. **Actualizar credenciales** usando variables de entorno
3. **Compilar** con `mvn clean package`
4. **Desplegar WAR** en servidor Tomcat de producción
5. **Configurar HTTPS** y certificado SSL
6. **Configurar firewall** y reglas de seguridad

### Docker (Opcional)

```dockerfile
FROM tomcat:11-jdk17
COPY target/verificador-codigos.war /usr/local/tomcat/webapps/
EXPOSE 8080
CMD ["catalina.sh", "run"]
```

## 🧪 Testing

### Pruebas Manuales

1. **Código válido existente:** `PROD001` → Debería mostrar "Laptop Dell XPS 15"
2. **Código inválido:** `PROD999` → Debería mostrar "No existe"
3. **Campo vacío:** → Debería mostrar error de validación
4. **Caracteres especiales:** `PROD-001` → Validación HTML5 debería rechazar

### Pruebas con cURL

```bash
# Verificar que el servidor responde
curl http://localhost:8080/verificador-codigos/

# Probar el servlet (POST)
curl -X POST http://localhost:8080/verificador-codigos/verificarCodigo \
  -d "codigo=PROD001" \
  -H "Content-Type: application/x-www-form-urlencoded"
```

## 🤝 Contribuir

Las contribuciones son bienvenidas. Para contribuir:

1. **Fork** el proyecto
2. **Crear** una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. **Commit** tus cambios (`git commit -m 'Add: nueva funcionalidad'`)
4. **Push** a la rama (`git push origin feature/AmazingFeature`)
5. **Abrir** un Pull Request

### Guía de Estilo

- Usar **camelCase** para variables y métodos
- Usar **PascalCase** para clases
- Agregar **comentarios JavaDoc** a métodos públicos
- Seguir las convenciones de Java estándar

## 📝 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo [LICENSE](LICENSE) para más detalles.

```
MIT License

Copyright (c) 2025 [Tu Nombre]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction...
```

## 👥 Autores

- **jhon fernando murillo y nicolas garzon cuadrado** - _Desarrollo inicial_ - [@jfmurillom](https://github.com/jfmurillom)

## 🙏 Agradecimientos

- Oracle por Java
- Apache Software Foundation por Tomcat y Maven
- MySQL por el sistema de gestión de base de datos
- La comunidad de Stack Overflow

## 📞 Contacto

- **Proyecto:** https://github.com/jfmurillom/proyecto-codigo-verificador
- **Email:** jhonfer_88@hotmail.com
- **LinkedIn:** https://linkedin.com/in/jhon fernando murillo manrrique

## 📊 Estado del Proyecto

🟢 **Activo** - En desarrollo activo

---

**⭐ Si te gustó este proyecto, dale una estrella en GitHub!**
