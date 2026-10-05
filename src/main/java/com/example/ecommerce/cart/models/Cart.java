package com.example.ecommerce.cart.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("Cart")
public class Cart implements Serializable {

   @Id
  private String userId;

  private Map<Integer, Integer> items = new HashMap<>();
}
