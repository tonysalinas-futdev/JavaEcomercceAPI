package com.example.ecommerce.integrationTests.category;

import com.example.ecommerce.categories.categoryDTOs.CategoryDetailsDTO;
import com.example.ecommerce.categories.categoryDTOs.CreateCategoryDTO;
import com.example.ecommerce.categories.categoryDTOs.UpdateCategoryDTO;
import com.example.ecommerce.categories.model.Category;
import com.example.ecommerce.categories.repository.CategoryRepository;
import com.example.ecommerce.categories.service.CategoryService;
import com.example.ecommerce.products.DTOs.CreateProductDTO;
import com.example.ecommerce.products.services.ProductService;
import com.example.ecommerce.shared.exceptions.AlreadyExistsException;
import com.example.ecommerce.shared.exceptions.InvalidRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;


@SpringBootTest
@Sql(
        scripts = {"/clean.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class TestCategoryService {
    @Autowired private CategoryService service;
    @Autowired private CategoryRepository repo;
    @Autowired private ProductService productService;

    @Test
    void shouldCreateNewCategorySuccessfully(){
        CreateCategoryDTO dto = CreateCategoryDTO.builder()
                .name("Lacteos")
                .description("Categoria de lacteos")
                .build();

        CategoryDetailsDTO category = service.createCategory(dto);

        Assertions.assertEquals("Lacteos", category.name());
        Assertions.assertNotNull( category.id());
    }

    @Test
    void shouldFailToCreateCategoryWithExistingName(){
        CreateCategoryDTO dto = CreateCategoryDTO.builder()
                .name("Lacteos")
                .description("Categoria de lacteos")
                .build();

        service.createCategory(dto);
        Assertions.assertThrows(AlreadyExistsException.class, ()->service.createCategory(dto));


    }

    @Test
    void shouldUpdateCategorySuccessfully(){
        CreateCategoryDTO dto = CreateCategoryDTO.builder()
                .name("Lacteos")
                .description("Categoria de lacteos")
                .build();

        CategoryDetailsDTO category = service.createCategory(dto);
        UpdateCategoryDTO updateDTO = UpdateCategoryDTO.builder()
                .name("Helados")
                .description("Helados ricos")
                .build();


        service.updateCategory(updateDTO, category.id());
        Category updatedCategory = service.getEntityByIdOrThrow(category.id());

        Assertions.assertEquals("Helados", updatedCategory.getName());
        Assertions.assertEquals("Helados ricos", updatedCategory.getDescription());

    }

    @Test
    void shouldFailToUpdateCategoryWithExistingName(){
        CreateCategoryDTO dto = CreateCategoryDTO.builder()
                .name("Pants")
                .description("The category of pants")
                .build();

        CreateCategoryDTO dto2 = CreateCategoryDTO.builder()
                .name("Shirts")
                .description("Category of shirts")
                .build();

        CategoryDetailsDTO pants = service.createCategory(dto);
        CategoryDetailsDTO  shirts = service.createCategory(dto2);

        Assertions.assertThrows(AlreadyExistsException.class, ()-> service.updateCategory( UpdateCategoryDTO.builder()
                .name("Shirts").build(), pants.id()));


    }

    @Test
    void shouldFailToDeleteCategoryWithProducts(){
        CreateCategoryDTO dto = CreateCategoryDTO.builder()
                 .name("Pants")
                .description("The category of pants")
                .build();

        CategoryDetailsDTO pants = service.createCategory(dto);
        CreateProductDTO productDTO = CreateProductDTO.builder()
                .categoryId(pants.id())
                .name("Oversize pants")
                .price(50.00)
                .stock(20)
                .build();

        productService.createProduct(productDTO);

        Assertions.assertThrows(InvalidRequestException.class, ()-> service.deleteCategory(pants.id()));


    }
}
