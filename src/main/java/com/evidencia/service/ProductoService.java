package com.evidencia.service;

import com.evidencia.model.Producto;
import com.evidencia.repository.ProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la lógica de negocio relacionada con Productos. Esta capa se
 * sitúa entre el controlador (servlet) y el repositorio (acceso a datos).
 *
 * @Service indica que esta clase es un componente de Spring que contiene lógica
 * de negocio Spring la gestiona como un bean singleton (una sola instancia en
 * toda la aplicación)
 *
 * @author Tu Nombre
 * @version 2.0
 */
@Service
public class ProductoService {

    private static final Logger logger = LoggerFactory.getLogger(ProductoService.class);

    /**
     * Repository inyectado automáticamente por Spring mediante el constructor.
     *
     * @Autowired le dice a Spring que inyecte ProductoRepository
     * automáticamente. Spring busca un bean de tipo ProductoRepository y lo
     * inyecta aquí.
     *
     * Esto es Inversión de Control (IoC): - No creamos el repository con 'new
     * ProductoRepository()' - Spring lo crea y nos lo inyecta - Facilita
     * testing (podemos inyectar mocks) - Desacopla componentes
     */
    private final ProductoRepository productoRepository;

    /**
     * Constructor con inyección de dependencias. Spring llama a este
     * constructor y le pasa el ProductoRepository.
     *
     * @param productoRepository Repository a inyectar
     */
    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
        logger.info("ProductoService inicializado con éxito");
    }

    /**
     * Verifica si un código de producto existe en la base de datos. Este es el
     * método principal usado por el servlet/controlador.
     *
     * @param codigo Código a verificar (será normalizado)
     * @return Optional con el Producto si existe, vacío si no
     */
    public Optional<Producto> verificarCodigo(String codigo) {
        logger.debug("Verificando código: {}", codigo);

        // Validación de entrada
        if (codigo == null || codigo.trim().isEmpty()) {
            logger.warn("Intento de verificar código vacío o nulo");
            return Optional.empty();
        }

        // Normalizar código: trim y uppercase
        String codigoNormalizado = codigo.trim().toUpperCase();
        logger.debug("Código normalizado: {}", codigoNormalizado);

        // Delegar al repository
        Optional<Producto> producto = productoRepository.findByCodigo(codigoNormalizado);

        if (producto.isPresent()) {
            logger.info("Código verificado - EXISTE: {}", codigoNormalizado);
        } else {
            logger.info("Código verificado - NO EXISTE: {}", codigoNormalizado);
        }

        return producto;
    }

    /**
     * Busca un producto por su código. Método alternativo a verificarCodigo
     * (mismo comportamiento).
     *
     * @param codigo Código del producto
     * @return Optional con el Producto si existe
     */
    public Optional<Producto> buscarPorCodigo(String codigo) {
        return verificarCodigo(codigo);
    }

    /**
     * Obtiene todos los productos. Útil para futuras funcionalidades (listar
     * productos, reportes, etc.)
     *
     * @return Lista de todos los productos
     */
    public List<Producto> obtenerTodos() {
        logger.debug("Obteniendo todos los productos");
        return productoRepository.findAll();
    }

    /**
     * Busca un producto por su ID.
     *
     * @param id ID del producto
     * @return Optional con el Producto si existe
     */
    public Optional<Producto> buscarPorId(Integer id) {
        logger.debug("Buscando producto por ID: {}", id);

        if (id == null || id <= 0) {
            logger.warn("ID inválido: {}", id);
            return Optional.empty();
        }

        return productoRepository.findById(id);
    }

    /**
     * Guarda un nuevo producto. Incluye validaciones de negocio.
     *
     * @param producto Producto a guardar
     * @return Producto guardado
     * @throws IllegalArgumentException si el producto es inválido
     */
    public Producto guardarProducto(Producto producto) {
        logger.debug("Guardando producto: {}", producto);

        // Validaciones de negocio
        validarProducto(producto);

        // Verificar que no exista un producto con el mismo código
        if (productoRepository.existsByCodigo(producto.getCodigo())) {
            String mensaje = "Ya existe un producto con el código: " + producto.getCodigo();
            logger.error(mensaje);
            throw new IllegalArgumentException(mensaje);
        }

        return productoRepository.save(producto);
    }

    /**
     * Actualiza un producto existente.
     *
     * @param producto Producto con datos actualizados
     * @return Producto actualizado
     * @throws IllegalArgumentException si el producto es inválido
     */
    public Producto actualizarProducto(Producto producto) {
        logger.debug("Actualizando producto: {}", producto);

        validarProducto(producto);

        // Verificar que el producto existe
        if (producto.getId() == null || !productoRepository.findById(producto.getId()).isPresent()) {
            String mensaje = "No se puede actualizar un producto que no existe";
            logger.error(mensaje);
            throw new IllegalArgumentException(mensaje);
        }

        return productoRepository.update(producto);
    }

    /**
     * Elimina un producto por su ID.
     *
     * @param id ID del producto a eliminar
     */
    public void eliminarProducto(Integer id) {
        logger.debug("Eliminando producto con ID: {}", id);

        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido");
        }

        productoRepository.deleteById(id);
    }

    /**
     * Cuenta el total de productos.
     *
     * @return Número de productos en la base de datos
     */
    public long contarProductos() {
        return productoRepository.count();
    }

    /**
     * Valida los datos básicos de un producto. Lógica de negocio centralizada.
     *
     * @param producto Producto a validar
     * @throws IllegalArgumentException si el producto es inválido
     */
    private void validarProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }

        if (producto.getCodigo() == null || producto.getCodigo().trim().isEmpty()) {
            throw new IllegalArgumentException("El código del producto es obligatorio");
        }

        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }

        // Validar longitud del código
        if (producto.getCodigo().length() > 50) {
            throw new IllegalArgumentException("El código no puede tener más de 50 caracteres");
        }

        // Validar longitud del nombre
        if (producto.getNombre().length() > 100) {
            throw new IllegalArgumentException("El nombre no puede tener más de 100 caracteres");
        }

        // Validar formato del código (solo alfanumérico)
        if (!producto.getCodigo().matches("[A-Za-z0-9]+")) {
            throw new IllegalArgumentException("El código solo puede contener letras y números");
        }
    }
}
