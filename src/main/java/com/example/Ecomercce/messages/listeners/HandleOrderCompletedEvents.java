package com.example.Ecomercce.messages.listeners;

import com.example.Ecomercce.messages.services.EmailSender;
import com.example.Ecomercce.messages.structure.EmailMessageData;
import com.example.Ecomercce.order.events.OrderCompletedEvent;
import com.example.Ecomercce.users.models.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class HandleOrderCompletedEvents {
  @Qualifier("emailSender")
  private final EmailSender sender;

  @EventListener
  public void sendEmailToUser(OrderCompletedEvent event) {
    User user = event.getOrder().getUser();
    String text = "Your payment has been completed, your order will be delivered in a few days";

    EmailMessageData data =
        EmailMessageData.builder()
            .to(user.getEmail())
            .subject("Payment Completed")
            .text(text)
            .build();

    sender.sendMessage(data);
  }
}
