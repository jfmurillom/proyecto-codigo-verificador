package com.evidencia.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad JPA que representa un producto en la base de datos. Mapea la tabla
 * 'productos' usando Hibernate.
 *
 * @author Tu Nombre
 * @version 2.0
 */
@Entity
@Table(name = "productos")
public class Producto {

    /**
     * ID único del producto (clave primaria, generada automáticamente)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * Código único del producto (ej: PROD001) No puede ser nulo y debe ser
     * único en la base de datos
     */
    @Column(name = "codigo", nullable = false, unique = true, length = 50)
    private String codigo;

    /**
     * Nombre descriptivo del producto No puede ser nulo
     */
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    /**
     * Fecha y hora de registro del producto Se establece automáticamente al
     * crear el registro
     */
    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro;

    // ========== CONSTRUCTORES ==========
    /**
     * Constructor por defecto requerido por JPA/Hibernate
     */
    public Producto() {
    }

    /**
     * Constructor con parámetros principales
     *
     * @param codigo Código del producto
     * @param nombre Nombre del producto
     */
    public Producto(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    /**
     * Constructor completo
     *
     * @param id ID del producto
     * @param codigo Código del producto
     * @param nombre Nombre del producto
     * @param fechaRegistro Fecha de registro
     */
    public Producto(Integer id, String codigo, String nombre, LocalDateTime fechaRegistro) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.fechaRegistro = fechaRegistro;
    }

    // ========== CALLBACK DE JPA ==========
    /**
     * Se ejecuta automáticamente antes de persistir la entidad Establece la
     * fecha de registro si no está definida
     */
    @PrePersist
    protected void onCreate() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
    }

    // ========== GETTERS Y SETTERS ==========
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    // ========== EQUALS, HASHCODE Y TOSTRING ==========
    /**
     * Dos productos son iguales si tienen el mismo código
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Producto producto = (Producto) o;
        return Objects.equals(codigo, producto.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return "Producto{"
                + "id=" + id
                + ", codigo='" + codigo + '\''
                + ", nombre='" + nombre + '\''
                + ", fechaRegistro=" + fechaRegistro
                + '}';
    }
}
