package com.evidencia.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManagerFactory;

/**
 * Configuración principal de Spring Framework. Esta clase reemplaza el XML
 * tradicional (applicationContext.xml).
 *
 * @Configuration indica que esta clase contiene beans de Spring
 * @ComponentScan le dice a Spring dónde buscar componentes (@Service,
 * @Repository, etc.)
 * @EnableTransactionManagement habilita el manejo de transacciones con
 * @Transactional
 *
 * @author Tu Nombre
 * @version 2.0
 */
@Configuration
@ComponentScan(basePackages = {
    "com.evidencia.service", // Escanea servicios
    "com.evidencia.repository", // Escanea repositorios
    "com.evidencia.servlets", // Escanea servlets
    "com.evidencia.controller"
})
@EnableTransactionManagement // Habilita @Transactional
public class AppConfig {

    /**
     * Configuración del DataSource (fuente de datos) usando HikariCP. HikariCP
     * es un connection pool de alto rendimiento.
     *
     * @Bean indica que este método produce un bean gestionado por Spring Spring
     * llamará a este método una vez y guardará el resultado
     *
     * Connection Pool: Mantiene conexiones abiertas reutilizables En lugar de
     * abrir/cerrar conexión en cada petición: - Abre un pool de N conexiones al
     * iniciar - Las reutiliza entre peticiones - Mucho más eficiente
     */
    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();

        // Configuración de conexión a MySQL
        config.setJdbcUrl("jdbc:mysql://localhost:3306/verificador_codigos?useSSL=false&serverTimezone=UTC");
        config.setUsername("root");
        config.setPassword("c24n8pmrsql"); // ⚠️ CAMBIAR según tu configuración
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");

        // Configuración del pool de conexiones
        config.setMaximumPoolSize(10);         // Máximo 10 conexiones
        config.setMinimumIdle(2);              // Mínimo 2 conexiones inactivas
        config.setConnectionTimeout(30000);     // 30 segundos timeout
        config.setIdleTimeout(600000);          // 10 minutos idle
        config.setMaxLifetime(1800000);         // 30 minutos máximo de vida

        // Configuración de validación de conexiones
        config.setConnectionTestQuery("SELECT 1");
        config.setValidationTimeout(5000);

        // Nombre del pool (para logs)
        config.setPoolName("HikariPool-VerificadorCodigos");

        return new HikariDataSource(config);
    }

    /**
     * Configuración del EntityManagerFactory de JPA/Hibernate. EntityManager es
     * la interfaz principal de JPA para operaciones de base de datos.
     *
     * Este bean: - Configura Hibernate - Escanea las entidades (@Entity) -
     * Establece propiedades de Hibernate
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        // Establecer el DataSource
        em.setDataSource(dataSource());

        // Paquete donde están las entidades (@Entity)
        em.setPackagesToScan("com.evidencia.model");

        // Configurar el proveedor JPA (Hibernate)
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(false);  // No generar tablas automáticamente
        vendorAdapter.setShowSql(true);       // Mostrar SQL en logs
        em.setJpaVendorAdapter(vendorAdapter);

        // Propiedades adicionales de Hibernate
        em.setJpaProperties(hibernateProperties());

        return em;
    }

    /**
     * Propiedades específicas de Hibernate. Configuran el comportamiento de
     * Hibernate.
     *
     * @return Properties con configuración de Hibernate
     */
    private Properties hibernateProperties() {
        Properties properties = new Properties();

        // Dialecto de MySQL (permite a Hibernate generar SQL optimizado para MySQL)
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");

        // Mostrar SQL formateado en la consola (útil para desarrollo)
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.format_sql", "true");

        // Mostrar comentarios en el SQL (útil para debugging)
        properties.setProperty("hibernate.use_sql_comments", "true");

        // Estrategia de generación de esquema
        // - none: No hacer nada (usar en producción)
        // - validate: Validar que el esquema coincida
        // - update: Actualizar el esquema automáticamente (usar con cuidado)
        // - create: Crear el esquema (borra datos existentes)
        // - create-drop: Crear al inicio, borrar al final
        properties.setProperty("hibernate.hbm2ddl.auto", "validate");

        // Configuración de cache (segundo nivel) - deshabilitado por defecto
        properties.setProperty("hibernate.cache.use_second_level_cache", "false");
        properties.setProperty("hibernate.cache.use_query_cache", "false");

        // Configuración de batch (para inserciones masivas eficientes)
        properties.setProperty("hibernate.jdbc.batch_size", "20");

        // Logging de estadísticas de Hibernate (útil para monitoreo)
        properties.setProperty("hibernate.generate_statistics", "false");

        return properties;
    }

    /**
     * Configuración del Transaction Manager. Gestiona las transacciones de base
     * de datos automáticamente.
     *
     * Cuando usas @Transactional en un método: - Spring inicia una transacción
     * antes del método - Si el método termina sin errores: COMMIT - Si hay una
     * excepción: ROLLBACK
     *
     * @param entityManagerFactory Factory inyectado por Spring
     * @return PlatformTransactionManager configurado
     */
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
