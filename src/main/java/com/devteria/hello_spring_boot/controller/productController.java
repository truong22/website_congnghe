package com.devteria.hello_spring_boot.controller;

import com.devteria.hello_spring_boot.Entity.Products;
import com.devteria.hello_spring_boot.Exceptions.ProductNotFoundException;
import com.devteria.hello_spring_boot.Exceptions.ResoureeNotFoundException;
import com.devteria.hello_spring_boot.Request.AddProductRequest;
import com.devteria.hello_spring_boot.Request.UpdateProductRequest;
import com.devteria.hello_spring_boot.Response.ApiResponse;
import com.devteria.hello_spring_boot.Service.Products.IProductService;
import com.devteria.hello_spring_boot.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;
@RequiredArgsConstructor
@Controller
@RequestMapping("${api.prefix}/products/")
public class productController   {
    private final IProductService iProductService;

    @GetMapping("{id}/product")
    public ResponseEntity<ApiResponse> productById(@PathVariable Long id){
        try {
            Products products = iProductService.getProductById(id);
            ProductDto productDto = iProductService.convertToDto(products);
            return ResponseEntity.ok(new ApiResponse("ok" + iProductService.getProductById(id), productDto));
        }   catch (ProductNotFoundException e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),INTERNAL_SERVER_ERROR));
        }
    }
    @GetMapping("all/product")
    public ResponseEntity<ApiResponse> productByAll(){
        try{
            List<Products> products=iProductService.getProductByAll();
            List<ProductDto>products1=iProductService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse( "ok",products1));
        }
        catch (Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),INTERNAL_SERVER_ERROR));
        }
    }
    @GetMapping("product/{nameProduct}/nameProduct")
    public ResponseEntity<ApiResponse> productByNameProduct(@PathVariable String nameProduct){
        try{
            List<Products> products=iProductService.getProductByNameProduct(nameProduct);
            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("not found name",null));
            } List<ProductDto>products1=iProductService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse( "ok",products1));
        }
        catch (ProductNotFoundException e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),INTERNAL_SERVER_ERROR));
        }
    }
    @GetMapping("product/{nameCategory}")
    public ResponseEntity<ApiResponse> productByCategory(@PathVariable String nameCategory){
        try{
            List<Products> products=iProductService.getProductByCategory(nameCategory);
            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("not found category",null));
            } List<ProductDto>products1=iProductService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse( "ok",products1));
        }
        catch (ProductNotFoundException e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),INTERNAL_SERVER_ERROR));
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("product/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest request){
        try{
            Products products=iProductService.addProduct(request);
            ProductDto productDto = iProductService.convertToDto(products);
            return ResponseEntity.ok(new ApiResponse( "ok",productDto));
        }
        catch (RuntimeException e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),INTERNAL_SERVER_ERROR));
        }
    }  @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("product/update/{id}")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody UpdateProductRequest request,@PathVariable Long id){
        try{
            Products products=iProductService.updateProduct(request,id);
            ProductDto productDto = iProductService.convertToDto(products);
            return ResponseEntity.ok(new ApiResponse( "ok",productDto));
        }
        catch (ResoureeNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }  @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("product/delete/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id){
        try{
         iProductService.deleteProduct(id);

            return ResponseEntity.ok(new ApiResponse( "ok",null));
        }
        catch (ResoureeNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @GetMapping("product/{brand}/brand")
    public ResponseEntity<ApiResponse> productByBrand(@PathVariable String brand){
        try{
            List<Products> products=iProductService.getProductByBrand(brand);
            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("not found name",null));
            } List<ProductDto>products1=iProductService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse( "ok",products1));
        }
        catch (ProductNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @GetMapping("product/categoryAndBrand")
    public ResponseEntity<ApiResponse> productByCategoryAndBrand(@RequestParam String nameCategory,@RequestParam String brand){
        try{
            List<Products> products=iProductService.getProductByCategoryAndBrand(nameCategory,brand);
            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("not found name",null));
            } List<ProductDto>products1=iProductService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse( "ok",products1));
        }
        catch (ProductNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @GetMapping("product/ProductAndBrand")
    public ResponseEntity<ApiResponse> productByProductAndBrand(@RequestParam String nameProduct,@RequestParam String brand){
        try{
            List<Products> products=iProductService.getProductByNameProductAndBrand(nameProduct,brand);
            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("not found name",null));
            } List<ProductDto>products1=iProductService.getConvertedProducts(products);
            return ResponseEntity.ok(new ApiResponse( "ok",products1));
        }
        catch (ProductNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @GetMapping("product/" +
            "")
    public ResponseEntity<ApiResponse> CountProductAndBrand(@RequestParam String brand,@RequestParam String nameProduct){
        try{
           var countproduct=iProductService.countByBrandAndNameProduct(brand,nameProduct);

            return ResponseEntity.ok(new ApiResponse( "ok",countproduct));
        }
        catch (Exception e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }










}
