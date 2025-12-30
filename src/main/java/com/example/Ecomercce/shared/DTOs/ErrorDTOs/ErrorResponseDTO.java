package com.example.Ecomercce.shared.DTOs.ErrorDTOs;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponseDTO {
  private String message;
  private String url;
  private LocalDateTime datetime;

  public ErrorResponseDTO(String message, String url) {
    this.message = message;
    this.url = url;
    this.datetime = LocalDateTime.now();
  }
  ;
}
