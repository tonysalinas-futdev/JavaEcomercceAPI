package com.example.ecommerce.shared.postgre;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@Setter
@ConfigurationProperties(prefix = "spring.datasource")
public class PostgresProperties {
    private String name = "ecommerce_test_db";
    private String url;
    private String username;
    private String password;
}
