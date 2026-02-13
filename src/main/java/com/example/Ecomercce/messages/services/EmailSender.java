package com.example.Ecomercce.messages.services;

import com.example.Ecomercce.logging.service.LoggerService;
import com.example.Ecomercce.messages.interface_.MessageSender;
import com.example.Ecomercce.messages.structure.EmailMessageData;
import com.example.Ecomercce.messages.utils.BuildSimpleMailMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service("emailSender")
@AllArgsConstructor
public class EmailSender implements MessageSender<EmailMessageData> {
  private LoggerService logger;
  private JavaMailSender sender;

  @Override
  public void sendMessage(EmailMessageData data) {
    try {

      SimpleMailMessage message = BuildSimpleMailMessage.execute(data);
      sender.send(message);
    } catch (Exception e) {
      logger.logError("Failed to send email");
    }
  }
}
