package com.evidencia.repository;

import com.evidencia.model.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para acceso a datos de Producto usando Hibernate/JPA. Spring
 * gestiona automáticamente la inyección del EntityManager.
 *
 * @Repository indica que esta clase es un componente de Spring para acceso a
 * datos
 * @Transactional maneja las transacciones de base de datos automáticamente
 *
 * @author Tu Nombre
 * @version 2.0
 */
@Repository
@Transactional(readOnly = true) // Por defecto, operaciones de solo lectura
public class ProductoRepository {

    private static final Logger logger = LoggerFactory.getLogger(ProductoRepository.class);

    /**
     * EntityManager inyectado automáticamente por Spring. Es el equivalente a
     * la Session de Hibernate.
     *
     * @PersistenceContext le dice a Spring que inyecte el EntityManager
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Busca un producto por su código único. Usa JPQL (Java Persistence Query
     * Language) en lugar de SQL nativo.
     *
     * @param codigo Código del producto a buscar
     * @return Optional conteniendo el producto si existe, o vacío si no
     */
    public Optional<Producto> findByCodigo(String codigo) {
        logger.debug("Buscando producto con código: {}", codigo);

        try {
            // JPQL: Consulta orientada a objetos (usa nombres de clase y atributos)
            String jpql = "SELECT p FROM Producto p WHERE p.codigo = :codigo";

            TypedQuery<Producto> query = entityManager.createQuery(jpql, Producto.class);
            query.setParameter("codigo", codigo);

            Producto producto = query.getSingleResult();

            logger.info("Producto encontrado: {} - {}", producto.getCodigo(), producto.getNombre());
            return Optional.of(producto);

        } catch (NoResultException e) {
            // No se encontró el producto - esto NO es un error
            logger.debug("No se encontró producto con código: {}", codigo);
            return Optional.empty();

        } catch (Exception e) {
            // Error real en la consulta
            logger.error("Error al buscar producto con código: {}", codigo, e);
            throw new RuntimeException("Error al consultar la base de datos", e);
        }
    }

    /**
     * Busca un producto por su ID.
     *
     * @param id ID del producto
     * @return Optional conteniendo el producto si existe
     */
    public Optional<Producto> findById(Integer id) {
        logger.debug("Buscando producto con ID: {}", id);

        try {
            Producto producto = entityManager.find(Producto.class, id);
            return Optional.ofNullable(producto);
        } catch (Exception e) {
            logger.error("Error al buscar producto con ID: {}", id, e);
            throw new RuntimeException("Error al consultar la base de datos", e);
        }
    }

    /**
     * Obtiene todos los productos de la base de datos.
     *
     * @return Lista de todos los productos
     */
    public List<Producto> findAll() {
        logger.debug("Obteniendo todos los productos");

        try {
            String jpql = "SELECT p FROM Producto p ORDER BY p.nombre";
            TypedQuery<Producto> query = entityManager.createQuery(jpql, Producto.class);

            List<Producto> productos = query.getResultList();
            logger.info("Se encontraron {} productos", productos.size());

            return productos;
        } catch (Exception e) {
            logger.error("Error al obtener todos los productos", e);
            throw new RuntimeException("Error al consultar la base de datos", e);
        }
    }

    /**
     * Guarda un nuevo producto en la base de datos.
     *
     * @Transactional sin readOnly permite escritura
     *
     * @param producto Producto a guardar
     * @return Producto guardado con su ID generado
     */
    @Transactional // Operación de escritura
    public Producto save(Producto producto) {
        logger.debug("Guardando producto: {}", producto.getCodigo());

        try {
            entityManager.persist(producto);
            logger.info("Producto guardado exitosamente: {}", producto.getId());
            return producto;
        } catch (Exception e) {
            logger.error("Error al guardar producto: {}", producto.getCodigo(), e);
            throw new RuntimeException("Error al guardar en la base de datos", e);
        }
    }

    /**
     * Actualiza un producto existente.
     *
     * @param producto Producto a actualizar
     * @return Producto actualizado
     */
    @Transactional // Operación de escritura
    public Producto update(Producto producto) {
        logger.debug("Actualizando producto: {}", producto.getCodigo());

        try {
            Producto updated = entityManager.merge(producto);
            logger.info("Producto actualizado exitosamente: {}", updated.getId());
            return updated;
        } catch (Exception e) {
            logger.error("Error al actualizar producto: {}", producto.getCodigo(), e);
            throw new RuntimeException("Error al actualizar en la base de datos", e);
        }
    }

    /**
     * Elimina un producto por su ID.
     *
     * @param id ID del producto a eliminar
     */
    @Transactional // Operación de escritura
    public void deleteById(Integer id) {
        logger.debug("Eliminando producto con ID: {}", id);

        try {
            Producto producto = entityManager.find(Producto.class, id);
            if (producto != null) {
                entityManager.remove(producto);
                logger.info("Producto eliminado exitosamente: {}", id);
            } else {
                logger.warn("No se encontró producto con ID: {}", id);
            }
        } catch (Exception e) {
            logger.error("Error al eliminar producto con ID: {}", id, e);
            throw new RuntimeException("Error al eliminar de la base de datos", e);
        }
    }

    /**
     * Cuenta el total de productos en la base de datos.
     *
     * @return Número total de productos
     */
    public long  count() {
        logger.debug("Contando productos");

        try {
            String jpql = "SELECT COUNT(p) FROM Producto p";
            long count = entityManager.createQuery(jpql, long.class).getSingleResult();
            logger.debug("Total de productos: {}", count);
            return count;
        } catch (Exception e) {
            logger.error("Error al contar productos", e);
            throw new RuntimeException("Error al consultar la base de datos", e);
        }
    }

    /**
     * Verifica si existe un producto con el código dado.
     *
     * @param codigo Código a verificar
     * @return true si existe, false si no
     */
    public boolean existsByCodigo(String codigo) {
        logger.debug("Verificando existencia de código: {}", codigo);

        try {
            String jpql = "SELECT COUNT(p) FROM Producto p WHERE p.codigo = :codigo";
            Integer count = entityManager.createQuery(jpql, Integer.class)
                    .setParameter("codigo", codigo)
                    .getSingleResult();

            boolean exists = count > 0;
            logger.debug("Código {} existe: {}", codigo, exists);
            return exists;
        } catch (Exception e) {
            logger.error("Error al verificar existencia de código: {}", codigo, e);
            throw new RuntimeException("Error al consultar la base de datos", e);
        }
    }
}
