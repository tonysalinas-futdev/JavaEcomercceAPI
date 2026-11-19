package com.example.ecommerce.config.containers;


import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestContainerConfig {

        @Bean
        @ServiceConnection
        public PostgreSQLContainer postgreSQLContainer() {
            return new PostgreSQLContainer("postgres:17").withReuse(true);
        }

        @Bean
        @ServiceConnection(name = "redis")
        public GenericContainer<?> redisGenericCOntainer(){
            var redis = new GenericContainer<>("redis:latest")
                    .withExposedPorts(6379).withReuse(true);
            redis.start();

            return redis;

        }
    }


