package com.example.ecommerce.cart.repositories;

import com.example.ecommerce.cart.models.Cart;
import org.springframework.data.repository.CrudRepository;

public interface CartRepository extends CrudRepository<Cart, String> {}
