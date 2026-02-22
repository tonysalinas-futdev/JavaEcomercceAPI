package com.example.ecommerce.products.listeners;

import com.example.ecommerce.order.events.OrderCompletedEvent;
import com.example.ecommerce.order.models.OrderDetails;
import com.example.ecommerce.products.model.Product;
import com.example.ecommerce.products.services.ProductService;
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
          service.setStock(product.getId(), product.getStock() - itm.getQuantity());
        });
  }
}
