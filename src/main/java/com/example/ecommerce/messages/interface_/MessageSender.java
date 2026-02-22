package com.example.ecommerce.messages.interface_;

public interface MessageSender<T> {

  public void sendMessage(T messageData);
}
