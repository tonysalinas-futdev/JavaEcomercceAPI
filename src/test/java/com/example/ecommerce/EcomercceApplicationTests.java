package com.example.ecommerce;

import com.example.ecommerce.shared.cache.configuration.RedisConfig;
import com.example.ecommerce.shared.cache.configuration.RedisProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootTest()
@ComponentScan(
    excludeFilters =
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = {RedisConfig.class, RedisProperties.class}))
@EnableAutoConfiguration(
    exclude = {RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class})
class EcomercceApplicationTests {

  @Test
  void contextLoads() {}
}
