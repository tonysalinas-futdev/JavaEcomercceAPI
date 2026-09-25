package com.example.ecommerce.auth.config;

import com.example.ecommerce.users.models.User;
import com.example.ecommerce.users.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AuthConfig {
  private final UserRepository repo;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public UserDetailsService userdetailsService() {
    return username -> {
      User user =
          repo.findUserByEmail(username)
              .orElseThrow(() -> new UsernameNotFoundException("User not found"));
      List<String> authorities = new ArrayList<>();

      user.getRoles()
          .forEach(
              role -> {
                authorities.add("ROLE_" + role.getRoleEnum().name());
                role.getPermissions().forEach(p -> authorities.add(p.getName()));
              });

      return org.springframework.security.core.userdetails.User.builder()
          .username(user.getEmail())
          .password(user.getPassword())
          .authorities(authorities.toArray(new String[0]))
          .build();
    };
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userdetailsService());
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }
}
