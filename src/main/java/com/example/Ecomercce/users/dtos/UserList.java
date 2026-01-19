package com.example.Ecomercce.users.dtos;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserList {
  private Long id;

  private String name;

  private String email;

  private LocalDateTime createdAt;
}
