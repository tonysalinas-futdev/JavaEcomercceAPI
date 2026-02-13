package com.example.Ecomercce.products.listeners;

import com.example.Ecomercce.order.events.OrderCompletedEvent;
import com.example.Ecomercce.order.models.OrderDetails;
import com.example.Ecomercce.products.model.Product;
import com.example.Ecomercce.products.services.ProductService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class HandleOrderPaidEvent {
  private final ProductService service;

  @EventListener
  public void updateProductsStock(OrderCompletedEvent event) {
    List<OrderDetails> items = event.getOrder().getOrderDetails();

    items.forEach(
        itm -> {
          Product product = itm.getProduct();
          service.setStock(product, product.getStock() - itm.getQuantity());
        });
  }
}
