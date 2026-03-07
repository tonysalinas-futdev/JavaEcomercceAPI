package com.example.ecommerce.shared.cache.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@Setter
@ConfigurationProperties(prefix = "spring.redis")
public class RedisProperties {
  private String host;
  private Integer port;
  private String password;
  private Long timeout;
}
