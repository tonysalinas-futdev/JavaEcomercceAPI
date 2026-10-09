package com.example.ecommerce.products.exceptions;

import com.example.ecommerce.shared.exceptions.BasicException;

public class ProductUnavailableException extends BasicException {
    public ProductUnavailableException(String message) {
        super(message);
    }
}
