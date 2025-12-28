package com.example.Ecomercce.Services;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.example.Ecomercce.DTOs.PaginatedDtos.PaginatedResponseDTO;
import com.example.Ecomercce.DTOs.ProductDTOs.CreateProductDTO;
import com.example.Ecomercce.DTOs.ProductDTOs.ProductDetailsDTO;
import com.example.Ecomercce.DTOs.ProductDTOs.ProductListDTO;
import com.example.Ecomercce.DTOs.ProductDTOs.SearchProductDTO;
import com.example.Ecomercce.DTOs.ProductDTOs.UpdateProduct;
import com.example.Ecomercce.Mappers.ProductMappers;
import com.example.Ecomercce.Repositories.CategoryRepository;
import com.example.Ecomercce.Repositories.ProductRepository;
import com.example.Ecomercce.Specifications.ProductSpecifications;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import com.example.Ecomercce.Models.Category;
import com.example.Ecomercce.Models.Product;
import lombok.AllArgsConstructor;
import com.example.Ecomercce.Exceptions.AlreadyExistsException;
import com.example.Ecomercce.Exceptions.DatabaseErrorException;
import com.example.Ecomercce.Exceptions.InvalidRequestException;
import com.example.Ecomercce.Exceptions.NotFoundException;

@Service
@AllArgsConstructor
public class ProductService {
    private ProductRepository productRepo;
    private ProductMappers productMapper;
    private CategoryRepository categoryRepo;
    private static final Logger logger=LoggerFactory.getLogger(ProductService.class);
    
    @Transactional
    public ProductDetailsDTO createProduct(CreateProductDTO dto)throws AlreadyExistsException,InvalidRequestException,DatabaseErrorException{
        if (productRepo.getByName(dto.getName()).isPresent()) {
            throw new AlreadyExistsException("Ya existe un producto con ese nombre");}
        
        Product newProduct=productMapper.createProductDTOToEntity(dto);

        Category category=categoryRepo.findById(dto.getCategoryId()).orElseThrow(()-> new InvalidRequestException("La categoría asociada al producto no existe"));

        newProduct.setCategory(category);
        
        try
        {productRepo.saveAndFlush(newProduct);
            logger.info("Guardado el producto con id={}",newProduct.getId());
        }catch(PersistenceException ex)
        {   logger.error("No se ha podido guardar el producto de nombre {}, Error: {}",newProduct.getName(),ex.getMessage());
            throw new DatabaseErrorException("Error en la base de datos");
        }
        
        
        ProductDetailsDTO newDTO=productMapper.productToDetailsDTO(newProduct);


        return newDTO;}


    public ProductDetailsDTO getProductById(Long id)throws NotFoundException{
        Product product=productRepo.findById(id).orElseThrow(()-> new NotFoundException("No se ha encontrado ningún producto con ese id"));
        ProductDetailsDTO productDto=productMapper.productToDetailsDTO(product);
        return productDto;

    }

    public PaginatedResponseDTO<ProductListDTO> getAllProducts(Integer page, Integer size){
        if (page==null) {
            page=0;}

        if (size==null) {
            size=10;}    

        Pageable pageable=PageRequest.of(page, size);
        Page<Product> products=productRepo.findAll(pageable);
        List<ProductListDTO> productList=products.stream().map(p->productMapper.productToProductListDTO(p)).toList();
        return new PaginatedResponseDTO<ProductListDTO>(
            productList,
            products.hasNext() , 
            products.hasPrevious(), 
            products.getNumber(), 
            products.getTotalPages(),
            products.getTotalElements(),
            products.getNumberOfElements(), 
            products.getSize());


    }

    public void deleteProduct(Long id)throws NotFoundException,DatabaseErrorException{
        Product product=productRepo.findById(id).orElseThrow(()->new NotFoundException("No se ha encontrado ningún producto"));
        try{
            productRepo.delete(product);
            logger.info("Se ha eliminado el producto con Id={}", product.getId());
        }catch(PersistenceException ex){
            logger.error("Error en la eliminación del producto con id={}",product.getId());
            throw new DatabaseErrorException("Error en la base de datos");
        }
        
    }

    @Transactional
    public ProductDetailsDTO updateProduct(UpdateProduct dto, Long productId) throws NotFoundException,DatabaseErrorException         {
        Product product=productRepo.findById(productId).orElseThrow(()-> new NotFoundException("No se ha encontrado ningún producto con ese id"));

        Product updatedProduct=productMapper.updateEntity(dto, product);
        try{
            productRepo.save(updatedProduct);
            logger.info("Se han actualizado los datos del productos con Id={}",product.getId());
        
        }catch(PersistenceException ex){
            logger.error("Ha fallado la actualización del producto con id={}, Error: {}", product.getId(), ex.getMessage());
            throw new DatabaseErrorException("Error en la base de datos");
        }
        

        ProductDetailsDTO updatedDTO=productMapper.productToDetailsDTO(updatedProduct);
        return updatedDTO;
    }

    @Transactional
    public ProductDetailsDTO updateCategory(Long categoryId, Long productId)throws NotFoundException,DatabaseErrorException{
        Category category=categoryRepo.findById(categoryId).orElseThrow(()-> new NotFoundException("No se ha encontrado ninguna categoría con ese id"));

        Product product=productRepo.findById(productId).orElseThrow(()-> new NotFoundException("No se ha encontrado ningun producto con ese id"));
        
        product.setCategory(category);
        try{
            productRepo.save(product);
            logger.info("Se ha actualizado la categoría del producto con id={}", product.getId());
        
        }catch(PersistenceException ex){
            logger.error("Ha fallado la actualización de la categoría producto con id={}, Error: {}", product.getId(), ex.getMessage());
            throw new DatabaseErrorException("Error en la base de datos");
        }
        

        ProductDetailsDTO productDTO=productMapper.productToDetailsDTO(product);
        return productDTO;
    }

    public PaginatedResponseDTO<ProductListDTO>searchProducts(SearchProductDTO dto) throws NotFoundException{

       

        Specification<Product> specification=ProductSpecifications
                .hasName(dto.getName())
                .and(ProductSpecifications.filterByMaxPrice(dto.getMaxPrice()))
                .and(ProductSpecifications.filterbyMinPrice(dto.getMinPrice()));
        
        if (dto.getCategoryId() != null) { Category category = categoryRepo.findById(dto.getCategoryId()).orElseThrow(() -> new NotFoundException("No se ha encontrado esa categoría")); 
        specification = specification.and(ProductSpecifications.filterByCategory(category)); }
                

        Page<Product> products=productRepo.findAll(
               specification ,PageRequest.of(dto.getPage(), dto.getSize()));

        List<ProductListDTO> listOfProductsDTO=products.stream()
        .map(p->productMapper.productToProductListDTO(p))
        .toList();

        return PaginatedResponseDTO.<ProductListDTO>builder()
        .items(listOfProductsDTO)
        .hasNext(products.hasNext())
        .hasPrevious(products.hasPrevious())
        .page(products.getNumber())
        .size(products.getSize())
        .numberOfElementsInPage(products.getNumberOfElements())
        .totalPages(products.getTotalPages())
        .totalElements(products.getTotalElements())
        .build();

        
    }
}
