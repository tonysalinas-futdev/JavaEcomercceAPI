package com.example.Ecomercce.TestServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Ecomercce.Exceptions.NotFoundException;
import com.example.Ecomercce.Models.Category;
import com.example.Ecomercce.Models.Product;
import com.example.Ecomercce.Repositories.CategoryRepository;
import com.example.Ecomercce.Repositories.ProductRepository;

@Service
public class ServicesConftest {
    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private CategoryRepository categoryRepo;


    public Category returnCategory()throws NotFoundException{
        if (categoryRepo.getByName("Muebles").isEmpty()) {
        Category category=Category.builder()
        .name("Muebles")
        .description("Muebles para la casa")
        .pic("fsd")
        .build();

        categoryRepo.save(category);
        return category;
        
        }
        else return categoryRepo.getByName("Muebles").orElseThrow(()->new NotFoundException("No se ha encontrado la categoría"));

        
    }

    public void registerProduct()throws NotFoundException{
        Category category=returnCategory();
        if (productRepo.getByName("Helado").isEmpty()) {
        Product producto= Product
        .builder()
        .name("Helado")
        .description("Un rico helado")
        .price(40.0) 
        .stock(50)
        .category(category)
        .build();

        productRepo.save(producto);
            
        }
        

    }   
}
