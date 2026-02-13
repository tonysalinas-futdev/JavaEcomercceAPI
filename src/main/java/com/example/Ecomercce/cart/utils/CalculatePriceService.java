package com.example.Ecomercce.cart.utils;

import com.example.Ecomercce.cart.models.Cart;
import com.example.Ecomercce.cart.models.CartItem;
import java.util.List;

public class CalculatePriceService {

  public static Double calculateTotalAmount(Cart cart) {
    List<CartItem> items = cart.getItems();
    double totalAmount =
        items.stream()
            .mapToDouble(
                itm -> {
                  Double price = itm.getProduct().getPrice();
                  Integer quantity = itm.getQuantity();
                  return price * quantity;
                })
            .sum();

    return totalAmount;
  }
}
