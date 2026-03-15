package com.devteria.hello_spring_boot.Exceptions;

public class ProductNotFoundException extends RuntimeException {
        public ProductNotFoundException(String message){
            super(message);
        }

}
