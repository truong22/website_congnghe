package com.devteria.hello_spring_boot.controller;


import com.devteria.hello_spring_boot.Entity.Role;
import com.devteria.hello_spring_boot.Entity.User;
import com.devteria.hello_spring_boot.Request.LoginRequest;
import com.devteria.hello_spring_boot.Response.ApiResponse;
import com.devteria.hello_spring_boot.Response.JwtResponse;
import com.devteria.hello_spring_boot.Respository.RoleRepository;
import com.devteria.hello_spring_boot.Respository.UserRepository;
import com.devteria.hello_spring_boot.Security.User.shopUserDetails;
import com.devteria.hello_spring_boot.Security.jwt.jwtUtils;
import com.devteria.hello_spring_boot.Service.Cart.ICartService;
import com.devteria.hello_spring_boot.dto.RegisterWithRoleRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.hibernate.AnnotationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/auth")
public class authController  {
    private final AuthenticationManager authenticationManager;
    private final jwtUtils jwtUtils;
    private final UserRepository userRepository;
    private  final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ICartService cartService;

    @PostMapping("login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request){
        try{
            Authentication authentication=authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassWord()
                    ));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt=jwtUtils.generateTokenForUser(authentication);
            shopUserDetails userDetails=(shopUserDetails)authentication.getPrincipal();
            JwtResponse jwtResponse= new JwtResponse(userDetails.getId(),jwt);
            return  ResponseEntity.ok(new ApiResponse("Login Successful",jwtResponse));

        }catch (AuthenticationException e){
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @PostMapping("register")
    @Transactional
    public  ResponseEntity<ApiResponse> register(@Valid@RequestBody RegisterWithRoleRequest request)
    {
        if(userRepository.existsByEmail(request.getEmail())){
            return ResponseEntity.badRequest().body(new ApiResponse("Email already exists",null));
        }
        Role role=roleRepository.findByRoleName("ROLE_USER")
                .orElseThrow(()->new RuntimeException("ROLE_USER not found"));
        User user=new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassWord(passwordEncoder.encode(request.getPassWord()));
        user.setRoles(Set.of(role));
        User savedUser = userRepository.save(user);
        cartService.initializeNewCart(savedUser);
        return ResponseEntity.ok(new ApiResponse("User registered successfully",null));
    }
    @PostMapping("create-user")
    @Transactional
    public  ResponseEntity<ApiResponse> createUserWithRole(@Valid@RequestBody RegisterWithRoleRequest request)
    {
        if(userRepository.existsByEmail(request.getEmail())){
            return ResponseEntity.badRequest().body(new ApiResponse("Email already exists",null));
        }
        Role role=roleRepository.findByRoleName(request.getRole())
                .orElseThrow(()->new RuntimeException("ROLE"+request.getRole()+"Not Found"));
        User user=new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassWord(passwordEncoder.encode(request.getPassWord()));
        user.setRoles(Set.of(role));
        User savedUser = userRepository.save(user);
        cartService.initializeNewCart(savedUser);
        return ResponseEntity.ok(new ApiResponse(
                "User With role"+request.getRole()+" created successfully",null));
    }
}
