package com.example.Ecomercce.Specifications;

import org.springframework.data.jpa.domain.Specification;

import com.example.Ecomercce.Models.Category;
import com.example.Ecomercce.Models.Product;

public class ProductSpecifications {
    public static Specification<Product>hasName(String name){
        return (root, query, cb)->
        name==null ? cb.conjunction(): cb.like(cb.lower(root.get("name")), "%"+name.toLowerCase()+"%");
    }

    public static Specification<Product>filterbyMinPrice(Double minPrice){
        return (root, query, cb)->
        minPrice==null ? cb.conjunction(): cb.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Product>filterByMaxPrice(Double maxPrice){
        return (root, query, cb)->
        maxPrice==null ? cb.conjunction(): cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<Product>filterByCategory(Category category){
        return (root, query, cb)->
        category==null ? cb.conjunction(): cb.equal(root.get("category"), category);
    }


}
