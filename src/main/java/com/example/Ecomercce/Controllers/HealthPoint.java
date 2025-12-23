package com.example.Ecomercce.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/healthpoint")
public class HealthPoint {
    @GetMapping("/")
    public String healthpoint(){
        return "Todo ok";
    }

}
