<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Resultado de Verificación</title>
    <link rel="stylesheet" href="estilos.css">
</head>
<body>
    <div class="container">
        <div class="card">
            <%
                // Obtener los atributos enviados desde el Servlet
                String codigo = (String) request.getAttribute("codigo");
                Boolean codigoExiste = (Boolean) request.getAttribute("codigoExiste");
                String nombreProducto = (String) request.getAttribute("nombreProducto");
                String mensajeError = (String) request.getAttribute("mensajeError");
                
                // Verificar si hay un error
                if (mensajeError != null) {
            %>
                <div class="resultado error">
                    <h2>⚠️ Error</h2>
                    <p><%= mensajeError %></p>
                    <a href="index.html" class="btn-volver">Volver a intentar</a>
                </div>
            <%
                } else if (codigoExiste != null && codigoExiste) {
                    // El código existe en la base de datos
            %>
                <div class="resultado exito">
                    <h2>✅ Código Encontrado</h2>
                    <p><strong>El código existe en el sistema</strong></p>
                    <div class="producto-info">
                        <p><strong>Código:</strong> <%= codigo %></p>
                        <p><strong>Producto:</strong> <%= nombreProducto %></p>
                    </div>
                    <a href="index.html" class="btn-volver">Verificar otro código</a>
                </div>
            <%
                } else {
                    // El código NO existe en la base de datos
            %>
                <div class="resultado error">
                    <h2>❌ Código No Encontrado</h2>
                    <p>El código <strong><%= codigo %></strong> no existe en el sistema</p>
                    <p style="margin-top: 15px; font-size: 0.95rem;">
                        Por favor, verifique que el código sea correcto e intente nuevamente.
                    </p>
                    <a href="index.html" class="btn-volver">Volver a intentar</a>
                </div>
            <%
                }
            %>
        </div>
        
        <footer class="footer">
            <p>Sistema de Verificación de Códigos v1.0</p>
        </footer>
    </div>
</body>
</html>