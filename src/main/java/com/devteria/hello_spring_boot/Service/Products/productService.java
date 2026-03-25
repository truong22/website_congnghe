package com.devteria.hello_spring_boot.Service.Products;

import com.devteria.hello_spring_boot.Entity.Category;
import com.devteria.hello_spring_boot.Entity.Products;
import com.devteria.hello_spring_boot.Entity.image;
import com.devteria.hello_spring_boot.Exceptions.ResoureeNotFoundException;
import com.devteria.hello_spring_boot.Request.AddProductRequest;
import com.devteria.hello_spring_boot.Request.UpdateProductRequest;
import com.devteria.hello_spring_boot.Respository.categoryRepository;
import com.devteria.hello_spring_boot.Respository.imageRepository;
import com.devteria.hello_spring_boot.Respository.productRepository;
import com.devteria.hello_spring_boot.dto.ImageDto;
import com.devteria.hello_spring_boot.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class productService implements IProductService {
    private final productRepository productRepository;
    private final categoryRepository  categoryRepository;
    private final imageRepository imageRepository;

    private final ModelMapper modelMapper;
    @Override
    public Products getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(
                ()->new ResoureeNotFoundException("not found id")
        );
    }

    @Override
    public List<Products> getProductByAll() {
        return productRepository.findAll();
    }

    @Override
    public List<Products> getProductByCategory(String category) {
        return productRepository.findByCategory_NameCategory(category);
    }

    @Override
    public List<Products> getProductByBrand(String brand) {
        return productRepository.findProductsByBrand(brand);
    }

    @Override
    public List<Products> getProductByNameProduct(String nameProduct) {
        return productRepository.findProductsByNameProduct(nameProduct);
    }

    @Override
    public List<Products> getProductByCategoryAndBrand(String category, String brand) {
        return productRepository. findByCategory_NameCategoryAndBrand(category,brand);
    }

    @Override
    public List<Products> getProductByNameProductAndBrand(String nameProduct, String brand) {
        return productRepository.findProductsByNameProductAndBrand(nameProduct,brand);
    }

    @Override
    public Long countByBrandAndNameProduct(String brand, String nameProduct) {
        return productRepository.countByBrandAndNameProduct(brand,nameProduct);
    }

    @Override
    public Products addProduct(AddProductRequest productRequest) {
        Category category = Optional.ofNullable
                (categoryRepository.findCategoryByNameCategory(
                        productRequest.getCategory().getNameCategory()
        )).orElseGet(()->{
            Category newCategory=new Category(productRequest.getCategory().getNameCategory());
            return categoryRepository.save(newCategory);
        });
        return productRepository.save(createProduct(productRequest,category));
    }
    private Products createProduct(AddProductRequest productRequest,Category category){
        if(productRequest.getInventory()<0){
            throw new RuntimeException("khong duoc am ");
        }
        return new Products(
            productRequest.getNameProduct(),
            productRequest.getBrand(),
            productRequest.getPrice(),
            productRequest.getInventory(),
            productRequest.getDescription(),
            category

        );
    }

    @Override
    public Products updateProduct(UpdateProductRequest productRequest, Long id) {
        return productRepository.findById(id).map(c->updateExistsProduct(productRequest,c))
                .map(productRepository::save)
                .orElseThrow(()->new ResoureeNotFoundException("not found id"));
    }
    private Products updateExistsProduct(UpdateProductRequest productRequest,Products products ){
        products.setNameProduct(   productRequest.getNameProduct());
        products.setBrand(productRequest.getBrand());
        products.setPrice(productRequest.getPrice());
        if(productRequest.getInventory()<0){
            throw new RuntimeException("khong < 0");
        }
        products.setInventory(productRequest.getInventory());
        products.setDescription(productRequest.getDescription());
        Category category=categoryRepository.findCategoryByNameCategory(productRequest.getCategory().getNameCategory());

        if (category == null) {
            throw new ResoureeNotFoundException("Không tìm thấy category: " + productRequest.getCategory().getNameCategory());}
        products.setCategory(category);
        return products;
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete,()->{
            throw new ResoureeNotFoundException("not found id");
        });

    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Products> products) {
        return products.stream().map(this::convertToDto).toList();
    }
    @Override
    public ProductDto convertToDto(Products products) {
        ProductDto productDto = modelMapper.map(products, ProductDto.class);

        List<image> images = imageRepository.findByProductsId(products.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image -> {
                    // ✅ Set downloadUrl giống như trong saveImages
                    ImageDto imageDto = modelMapper.map(image, ImageDto.class);
                    imageDto.setDownloadUrl("/api/v1/images/image/downloadUrl/" + image.getId());
                    return imageDto;
                })
                .toList();

        productDto.setImageDtos(imageDtos);
        return productDto;
    }

}
