package com.devteria.hello_spring_boot.controller;

import com.devteria.hello_spring_boot.Entity.User;
import com.devteria.hello_spring_boot.Exceptions.AlreadyExistsException;
import com.devteria.hello_spring_boot.Exceptions.ResoureeNotFoundException;
import com.devteria.hello_spring_boot.Request.CreateUserRequest;
import com.devteria.hello_spring_boot.Request.UpdateUserRequest;
import com.devteria.hello_spring_boot.Response.ApiResponse;
import com.devteria.hello_spring_boot.Service.user.IUserService;
import com.devteria.hello_spring_boot.dto.UserDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/users/")
public class UserController {
    private final IUserService userService;
    @GetMapping("{userId}/user")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        try {
            // Chỉ gọi đúng 1 hàm này, lấy thẳng DTO
            UserDto userDto = userService.getUserDtoById(userId);

            return ResponseEntity.ok(new ApiResponse("Success", userDto));
        } catch (ResoureeNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
    @PostMapping("add")
    public ResponseEntity<ApiResponse>addUser(@RequestBody CreateUserRequest request){
        try{
            User user= userService.createUser(request);
            UserDto userDto=userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse("Add Ok !",userDto));
        }catch (AlreadyExistsException e){
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @PutMapping("update/{userId}/user")
    public ResponseEntity<ApiResponse>updateUser(@RequestBody UpdateUserRequest request, @PathVariable Long userId){
        try{
            User user= userService.updateUser(request, userId);
            UserDto userDto=userService.convertUserToDto(user);
            return ResponseEntity.ok(new ApiResponse(" update Success",userDto));
        }catch (ResoureeNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @DeleteMapping("delete/user/{userId}")
    public ResponseEntity<ApiResponse>deleteUser(@PathVariable Long userId){
        try{
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("delete Ok!",null));
        }catch (ResoureeNotFoundException e){
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

}
