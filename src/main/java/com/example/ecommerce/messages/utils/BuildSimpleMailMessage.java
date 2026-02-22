package com.example.ecommerce.messages.utils;

import com.example.ecommerce.messages.structure.EmailMessageData;
import org.springframework.mail.SimpleMailMessage;

public class BuildSimpleMailMessage {

  public static SimpleMailMessage execute(EmailMessageData data) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setSubject(data.getSubject());
    message.setText(data.getText());
    message.setTo(data.getTo());

    return message;
  }
}
