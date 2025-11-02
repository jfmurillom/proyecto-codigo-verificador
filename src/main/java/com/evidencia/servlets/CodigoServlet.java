package com.evidencia.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet que maneja la verificación de códigos de productos en la base de
 * datos. VERSIÓN PARA TOMCAT 10+ (usa jakarta)
 */
@WebServlet("/verificarCodigo")
public class CodigoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // Configuración de la base de datos
    private static final String DB_URL = "jdbc:mysql://localhost:3306/verificador_codigos";
    private static final String DB_USER = "root"; // Cambiar según tu configuración
    private static final String DB_PASSWORD = "c24n8pmrsql"; // Cambiar según tu contraseña de MySQL

    /**
     * Método que se ejecuta cuando el servlet es inicializado. Carga el driver
     * de MySQL.
     */
    @Override
    public void init() throws ServletException {
        try {
            // Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver de MySQL cargado correctamente");
        } catch (ClassNotFoundException e) {
            throw new ServletException("No se pudo cargar el driver de MySQL", e);
        }
    }

    /**
     * Maneja las peticiones POST del formulario. Recibe el código, consulta la
     * base de datos y muestra el resultado.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Configurar la codificación de caracteres
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // Obtener el código ingresado por el usuario
        String codigo = request.getParameter("codigo");

        // Validar que el código no esté vacío
        if (codigo == null || codigo.trim().isEmpty()) {
            request.setAttribute("error", "El código no puede estar vacío");
            request.getRequestDispatcher("resultado.jsp").forward(request, response);
            return;
        }

        // Normalizar el código (convertir a mayúsculas y eliminar espacios)
        codigo = codigo.trim().toUpperCase();

        // Variables para almacenar los resultados
        boolean codigoExiste = false;
        String nombreProducto = null;
        String mensajeError = null;

        // Consultar la base de datos
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Establecer conexión con la base de datos
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Conexión establecida con la base de datos");

            // Preparar la consulta SQL
            String sql = "SELECT nombre FROM productos WHERE codigo = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, codigo);

            // Ejecutar la consulta
            rs = stmt.executeQuery();

            // Verificar si se encontró el código
            if (rs.next()) {
                codigoExiste = true;
                nombreProducto = rs.getString("nombre");
                System.out.println("Código encontrado: " + codigo + " - " + nombreProducto);
            } else {
                System.out.println("Código no encontrado: " + codigo);
            }

        } catch (SQLException e) {
            // Manejo de errores de base de datos
            mensajeError = "Error al consultar la base de datos: " + e.getMessage();
            System.err.println("Error SQL: " + e.getMessage());
            e.printStackTrace();

        } finally {
            // Cerrar recursos en orden inverso
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
                System.out.println("Recursos de base de datos cerrados correctamente");
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }

        // Enviar los resultados a la página JSP
        request.setAttribute("codigo", codigo);
        request.setAttribute("codigoExiste", codigoExiste);
        request.setAttribute("nombreProducto", nombreProducto);
        request.setAttribute("mensajeError", mensajeError);

        // Redirigir a la página de resultados
        request.getRequestDispatcher("resultado.jsp").forward(request, response);
    }

    /**
     * Maneja las peticiones GET redirigiendo a POST. Esto permite que el
     * servlet funcione con ambos métodos.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirigir a la página principal si se accede directamente
        response.sendRedirect("index.html");
    }
}
