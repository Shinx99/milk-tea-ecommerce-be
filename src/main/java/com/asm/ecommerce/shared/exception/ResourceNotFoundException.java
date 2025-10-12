package com.asm.ecommerce.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue){
        super((String.format("%s Id not found %s", resourceName, fieldName, fieldValue)));

    }

    public ResourceNotFoundException(String resourceName, UUID id){
        super(String.format("%s Id not found %s", resourceName, id));
    }

    public ResourceNotFoundException(String message){
        super(message);
    }
}
