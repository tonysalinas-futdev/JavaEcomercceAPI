package com.example.Ecomercce.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.example.Ecomercce.DTOs.CategoryDTOs.CategoryDetailsDTO;
import com.example.Ecomercce.DTOs.CategoryDTOs.CategoryListDTO;
import com.example.Ecomercce.DTOs.CategoryDTOs.CreateCategoryDTO;
import com.example.Ecomercce.DTOs.CategoryDTOs.UpdateCategory;
import com.example.Ecomercce.Exceptions.AlreadyExistsException;
import com.example.Ecomercce.Exceptions.DatabaseErrorException;
import com.example.Ecomercce.Exceptions.InvalidRequestException;
import com.example.Ecomercce.Exceptions.NotFoundException;
import com.example.Ecomercce.Services.CategoryService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@Validated
@RequestMapping("categories/")
public class CategoryController {
    @Autowired
    CategoryService service;

    @GetMapping("get_category/{id}")
    public ResponseEntity<CategoryDetailsDTO> getCategory(@PathVariable @Positive Long id)throws NotFoundException{
        return ResponseEntity.status(200).body(service.getCategoryDTOById(id));
    }

    @GetMapping("get_all_categories")
    public ResponseEntity<List<CategoryListDTO>>getAllCategories(){
        return ResponseEntity.status(200).body(service.getAllCategories());
    }

    @PostMapping("create_category")
    public ResponseEntity<CategoryDetailsDTO>createCategory(@RequestBody @Valid CreateCategoryDTO dto)throws AlreadyExistsException,DatabaseErrorException{
        return ResponseEntity.status(201).body(service.createCategory(dto));
    }

    @PatchMapping("update_category/{id}")
    public ResponseEntity<CategoryDetailsDTO>updateCategory(@RequestBody @Valid UpdateCategory dto, @PathVariable @Positive  Long id) throws NotFoundException,DatabaseErrorException
    {
        return ResponseEntity.status(200).body(service.updateCategory(dto, id));
    }

    @DeleteMapping("delete_product/{id}")
    public ResponseEntity<?>deleteCategory(@Positive @PathVariable Long id)throws NotFoundException, InvalidRequestException,DatabaseErrorException{
        service.deleteCategory(id);
        return ResponseEntity.ok("La categoría se ha eliminado exitosamente");
    }


}
