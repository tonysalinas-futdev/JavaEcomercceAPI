package com.example.Ecomercce.TestServices;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import com.example.Ecomercce.DTOs.PaginatedDtos.PaginatedResponseDTO;
import com.example.Ecomercce.DTOs.ProductDTOs.CreateProductDTO;
import com.example.Ecomercce.DTOs.ProductDTOs.ProductDetailsDTO;
import com.example.Ecomercce.DTOs.ProductDTOs.ProductListDTO;
import com.example.Ecomercce.DTOs.ProductDTOs.SearchProductDTO;
import com.example.Ecomercce.DTOs.ProductDTOs.UpdateProduct;
import com.example.Ecomercce.Exceptions.AlreadyExistsException;
import com.example.Ecomercce.Exceptions.InvalidRequestException;
import com.example.Ecomercce.Exceptions.NotFoundException;
import com.example.Ecomercce.Models.Category;
import com.example.Ecomercce.Models.Product;
import com.example.Ecomercce.Repositories.CategoryRepository;
import com.example.Ecomercce.Repositories.ProductRepository;
import com.example.Ecomercce.Services.ProductService;


@SpringBootTest
public class ProductServiceTest {
    @Autowired
    private ProductService service;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private ServicesConftest conftest;

    @Autowired
    private ProductRepository productRepo;

    

    @ParameterizedTest
    @CsvSource({
        "Cama, Una cama suave, dhfghfhg, 200.00, 50",
        "Armario,, ddsfsd, 50.0, 45",
        "Silla,,,400.0,"
    })
    void testCreateProductHappyPath(String name, String description, String pic, Double price, Integer stock) throws AlreadyExistsException, InvalidRequestException, NotFoundException{
        
        Category category=conftest.returnCategory();

        CreateProductDTO dto=CreateProductDTO
        .builder()
        .name(name)
        .description(description)
        .pic(pic)
        .price(price)
        .stock(stock)
        .categoryId(category.getId())
        .build();

        
        ProductDetailsDTO product=service.createProduct(dto);

        Optional<Product> refreshedProduct=productRepo.getByName(name);


        assertTrue(product.getId()!=null);
        if (refreshedProduct.isPresent()) {
            assertTrue(refreshedProduct.get().getName().equals(name));
            assertTrue(refreshedProduct.get().getCreatedAt()!=null);
            assertTrue(refreshedProduct.get().getStock()==stock);}}
        
    @ParameterizedTest
    @CsvSource({
        "null, Un bonito armario, fsfsgs, 20.0, 40",
        "Helado, Un rico helado,, 60.0, 100",
        "Botas de agua, Muy buenas botas, eqweq, 70.0, 57"
    })
    void testCreateProductFailPath(String name, String description, String pic, Double price, Integer stock)throws NotFoundException{

        conftest.registerProduct();
        Category category=conftest.returnCategory();
        CreateProductDTO product=CreateProductDTO
        .builder()
        .name(name)
        .description(description)
        .pic(pic)
        .price(price)
        .stock(stock)
        .categoryId(category.getId())
        .build();



        if (name==null) {
            assertThrows(InvalidRequestException.class, ()->{service.createProduct(product);});
            
        }
        else if(name.equals("Helado")){
            assertThrows(AlreadyExistsException.class, ()->{service.createProduct(product);});
        }

        else if(name.equals("Botas de agua")){
            product.setCategoryId(Long.valueOf(40));
            assertThrows(InvalidRequestException.class, ()->{service.createProduct(product);});
        }
    }

    @ParameterizedTest
    @CsvSource({
        "Chocolate, Un rico chocolate, dhfghfhg, 200.00, 50",
        "TV LG,, ddsfsd, 50.0, 45",
        "Coche Audi,,,400.0,"
    })
    void testUpdateProductService(String name, String description, String pic, Double price, Integer stock)throws NotFoundException{
        conftest.registerProduct();
        Product product=productRepo.getByName("Helado").orElseThrow(()-> new NotFoundException("No se ha encontrado el producto"));

        UpdateProduct updateData=UpdateProduct.builder().name(name).description(description).pic(pic).price(price).stock(stock).build();

        ProductDetailsDTO updatedProduct=service.updateProduct(updateData, product.getId());

        assertTrue(updatedProduct.getName().equals(name));
        assertEquals(product.getId(), updatedProduct.getId());

        if (description!=null) {
            assertTrue(updatedProduct.getDescription().equals(description));
            
            
        }else{
            assertTrue(updatedProduct.getDescription().equals(product.getDescription()));
        }
        
    
        if (price!=null) {
            assertEquals(updatedProduct.getPrice(), price);
            
            
        }else{
            assertEquals(updatedProduct.getPrice(), product.getPrice());
        }
    
    }

    @Test
    @Sql("/data.sql")
    void testSearchProductsService()throws NotFoundException{
        SearchProductDTO data=SearchProductDTO.builder().maxPrice(130.0).minPrice(40.0).build();
        PaginatedResponseDTO<ProductListDTO> products=service.searchProducts(data);

        System.out.println(products);
        assertTrue(products.getSize()>0);
    }
}
