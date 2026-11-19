package com.example.ecommerce.config.containers;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;

public abstract class DockerContainersBase {
    static final PostgreSQLContainer postgres= new PostgreSQLContainer("postgres:17");

    static {
        postgres.start();
    }
    @ServiceConnection(name = "redis")
    static public GenericContainer<?> redisGenericCOntainer(){
        var redis = new GenericContainer<>("redis:latest")
                .withExposedPorts(6379);
        redis.start();

        return redis;
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

    }
}
