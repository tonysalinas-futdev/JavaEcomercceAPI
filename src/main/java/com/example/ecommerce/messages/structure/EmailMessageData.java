package com.example.ecommerce.messages.structure;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class EmailMessageData {
  private String to;
  private String subject;
  private String text;
}
