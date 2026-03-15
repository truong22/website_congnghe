package com.devteria.hello_spring_boot.controller;


import com.devteria.hello_spring_boot.Entity.Category;
import com.devteria.hello_spring_boot.Exceptions.AlreadyExistsException;
import com.devteria.hello_spring_boot.Exceptions.ResoureeNotFoundException;
import com.devteria.hello_spring_boot.Response.ApiResponse;
import com.devteria.hello_spring_boot.Service.IcategoryService;
import static org.springframework.http.HttpStatus.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("${api.prefix}/categories/")
public class categoryController {
    private final IcategoryService icategoryService;
    @GetMapping("all")
    public ResponseEntity<ApiResponse>getCategoryByAll(){
        try{
            List<Category>categories= icategoryService.getCategoryAll();
            return ResponseEntity.ok(new ApiResponse("ok",categories));
        }catch (Exception e){
            return  ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Error",INTERNAL_SERVER_ERROR));
        }
    }
    @GetMapping("{id}/category")
    public ResponseEntity<ApiResponse>getCategoryById(@PathVariable Long id){
        try{
             Category category= icategoryService.getCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("ok",category));
        }catch (ResoureeNotFoundException e){
            return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @GetMapping("category/{nameCategory}")
    public ResponseEntity<ApiResponse>getCategoryByName(@PathVariable String nameCategory){
        try{
           Category categories= icategoryService.getCategoryByName(nameCategory);
            return ResponseEntity.ok(new ApiResponse("ok",categories));
        }catch (ResoureeNotFoundException e){
            return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @PostMapping("add")
    public ResponseEntity<ApiResponse>addCategory(@RequestBody Category category){
        try{
            Category categories= icategoryService.addCategory(category);
            return ResponseEntity.ok(new ApiResponse("Success",categories));
        }catch (AlreadyExistsException e){
            return  ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @PutMapping("category/{id}/update")
    public ResponseEntity<ApiResponse>updateCategory(@PathVariable Long id,@RequestBody Category category){
        try{
            Category categories= icategoryService.updateCategory(id,category);
            return ResponseEntity.ok(new ApiResponse(" update ok",categories));
        }catch (ResoureeNotFoundException e){
            return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @DeleteMapping("category/{id}/delete")
    public ResponseEntity<ApiResponse>deleteCategory(@PathVariable Long id){
        try{
            icategoryService.deleteCategory(id);
            return ResponseEntity.ok(new ApiResponse("ok",null));
        }catch (ResoureeNotFoundException e){
            return  ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

}
