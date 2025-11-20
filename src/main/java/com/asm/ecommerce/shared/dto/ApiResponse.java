package com.asm.ecommerce.shared.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;

    private String message;

    private T data;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Builder.Default
    private LocalDateTime timeStamp = LocalDateTime.now();

    // Success response với data
    public static <T> ApiResponse<T> success(T data){
        return ApiResponse.<T>builder()
                .success(true)
                .message("success")
                .data(data)
                .timeStamp(LocalDateTime.now())
                .build();

    }


    // Success response với custom message và data
    public static <T> ApiResponse<T> success(String message, T data){
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timeStamp(LocalDateTime.now())
                .build();

    }

    // Success response không có data
    public static <T> ApiResponse<T> success(String message){
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .timeStamp(LocalDateTime.now())
                .build();

    }


    // Error response
    public static <T> ApiResponse<T> error(String message){
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .timeStamp(LocalDateTime.now())
                .build();

    }


    // Error response với data (có thể chứa error details)
    public static <T> ApiResponse<T> error(String message, T data){
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(data)
                .timeStamp(LocalDateTime.now())
                .build();

    }
}

