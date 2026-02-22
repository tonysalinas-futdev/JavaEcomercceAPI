package com.example.ecommerce.messages.services;

import com.example.ecommerce.messages.interface_.MessageSender;
import com.example.ecommerce.messages.structure.EmailMessageData;
import com.example.ecommerce.messages.utils.BuildSimpleMailMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service("emailSender")
@Slf4j
@AllArgsConstructor
public class EmailSender implements MessageSender<EmailMessageData> {
  private JavaMailSender sender;

  @Override
  public void sendMessage(EmailMessageData data) {
    try {

      SimpleMailMessage message = BuildSimpleMailMessage.execute(data);
      sender.send(message);
      log.info("Email sent to " + data.getTo());

    } catch (Exception e) {
      log.warn("Failed to sent message to" + data.getTo());
    }
  }
}
