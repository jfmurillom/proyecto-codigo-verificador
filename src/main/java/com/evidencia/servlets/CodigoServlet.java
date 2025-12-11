package com.evidencia.servlets;

import com.evidencia.model.Producto;
import com.evidencia.service.ProductoService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;
import java.util.Optional;

/**
 * Servlet que maneja la verificación de códigos de productos.
 * VERSIÓN ACTUALIZADA CON SPRING + HIBERNATE
 * 
 * CAMBIOS PRINCIPALES:
 * - Ya NO usa JDBC directamente
 * - Ya NO crea conexiones manuales
 * - Usa Spring para inyección de dependencias
 * - Delega la lógica al ProductoService
 * - Usa Hibernate a través del service
 * 
 * FLUJO:
 * 1. Usuario envía formulario
 * 2. Servlet recibe la petición
 * 3. Servlet le pide a Spring el ProductoService
 * 4. Service usa ProductoRepository
 * 5. Repository consulta con Hibernate
 * 6. Servlet muestra resultado en JSP
 * 
 * @author Tu Nombre
 * @version 2.0 - Con Spring + Hibernate
 */
@WebServlet("/verificarCodigo")
public class CodigoServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(CodigoServlet.class);
    
    /**
     * Service inyectado por Spring.
     * NO se inicializa con 'new' - Spring lo inyecta automáticamente.
     * 
     * Se obtiene en el método init() usando el WebApplicationContext.
     */
    private ProductoService productoService;
    
    /**
     * Se ejecuta cuando el servlet es inicializado.
     * 
     * IMPORTANTE: Aquí obtenemos el ProductoService desde Spring.
     * WebApplicationContext es el contenedor de Spring en una aplicación web.
     * 
     * Spring ya inicializó todos los beans (@Service, @Repository, etc.)
     * Solo necesitamos pedirle el bean que necesitamos.
     */
    @Override
    public void init() throws ServletException {
        super.init();
        
        try {
            // Obtener el contexto de Spring
            // WebApplicationContextUtils busca el contexto de Spring en el ServletContext
            WebApplicationContext context = WebApplicationContextUtils
                    .getWebApplicationContext(getServletContext());
            
            if (context == null) {
                String error = "No se pudo obtener el contexto de Spring. Verifica la configuración.";
                logger.error(error);
                throw new ServletException(error);
            }
            
            // Obtener el bean ProductoService desde Spring
            // Spring busca un bean de tipo ProductoService y nos lo devuelve
            // Esto es INYECCIÓN DE DEPENDENCIAS en tiempo de ejecución
            productoService = context.getBean(ProductoService.class);
            
            if (productoService == null) {
                String error = "No se pudo obtener ProductoService desde Spring.";
                logger.error(error);
                throw new ServletException(error);
            }
            
            logger.info("CodigoServlet inicializado correctamente con Spring");
            logger.info("ProductoService inyectado: {}", productoService.getClass().getName());
            
        } catch (Exception e) {
            logger.error("Error al inicializar el servlet con Spring", e);
            throw new ServletException("Error en la inicialización", e);
        }
    }
    
    /**
     * Maneja las peticiones POST del formulario.
     * 
     * FLUJO SIMPLIFICADO:
     * 1. Recibir código del usuario
     * 2. Validar entrada
     * 3. Llamar a productoService.verificarCodigo()
     * 4. Enviar resultado al JSP
     * 
     * Ya NO hay:
     * - Connection, PreparedStatement, ResultSet
     * - try-catch con SQLException
     * - Manejo manual de recursos
     * - SQL hardcodeado
     * 
     * Spring y Hibernate manejan todo eso automáticamente.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Configurar encoding
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        
        // 1. OBTENER CÓDIGO DEL FORMULARIO
        String codigo = request.getParameter("codigo");
        logger.info("Petición recibida para verificar código: {}", codigo);
        
        // 2. VALIDAR ENTRADA
        if (codigo == null || codigo.trim().isEmpty()) {
            logger.warn("Código vacío o nulo recibido");
            request.setAttribute("error", "El código no puede estar vacío");
            request.getRequestDispatcher("resultado.jsp").forward(request, response);
            return;
        }
        
        // Normalizar código
        codigo = codigo.trim().toUpperCase();
        logger.debug("Código normalizado: {}", codigo);
        
        try {
            // 3. VERIFICAR CÓDIGO USANDO EL SERVICE
            // Esta es la ÚNICA línea que accede a la base de datos
            // Todo el resto lo maneja Spring + Hibernate automáticamente
            Optional<Producto> productoOpt = productoService.verificarCodigo(codigo);
            
            // 4. PREPARAR RESULTADO
            if (productoOpt.isPresent()) {
                // Código EXISTE
                Producto producto = productoOpt.get();
                
                logger.info("✅ Código encontrado: {} - {}", producto.getCodigo(), producto.getNombre());
                
                request.setAttribute("codigoExiste", true);
                request.setAttribute("codigo", producto.getCodigo());
                request.setAttribute("nombreProducto", producto.getNombre());
                request.setAttribute("producto", producto); // Objeto completo
                
            } else {
                // Código NO EXISTE
                logger.info("❌ Código no encontrado: {}", codigo);
                
                request.setAttribute("codigoExiste", false);
                request.setAttribute("codigo", codigo);
                request.setAttribute("nombreProducto", null);
            }
            
            // 5. REDIRIGIR AL JSP
            request.getRequestDispatcher("resultado.jsp").forward(request, response);
            
        } catch (Exception e) {
            // Manejo de errores
            logger.error("Error al verificar código: {}", codigo, e);
            
            request.setAttribute("error", "Error al consultar la base de datos: " + e.getMessage());
            request.setAttribute("codigo", codigo);
            request.getRequestDispatcher("resultado.jsp").forward(request, response);
        }
    }
    
    /**
     * Maneja las peticiones GET.
     * Redirige a la página principal.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.debug("Petición GET recibida, redirigiendo a index.html");
        response.sendRedirect("index.html");
    }
    
    /**
     * Se ejecuta cuando el servlet es destruido.
     * Spring se encarga de cerrar todos los recursos automáticamente.
     */
    @Override
    public void destroy() {
        logger.info("CodigoServlet destruido");
        // No necesitamos cerrar nada manualmente
        // Spring cierra el DataSource, EntityManager, etc.
        super.destroy();
    }
}