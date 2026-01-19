package com.example.Ecomercce.auth.utils;

import com.example.Ecomercce.auth.config.JwtProperties;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecretKeyProvider {
  private final JwtProperties properties;

  public SecretKey getTokenSecretKey() {
    byte[] bytes = Decoders.BASE64.decode(properties.getSecret());
    return Keys.hmacShaKeyFor(bytes);
  }
}
