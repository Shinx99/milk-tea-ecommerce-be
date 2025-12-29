package com.asm.ecommerce.shared.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private int status;

    private String error;

    private String message;

    private String path;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Builder.Default
    private LocalDateTime timeStamp = LocalDateTime.now();

    // Validation errors: field -> error message
    private Map<String, String> validationErrors;

    // Multiple error messages
    private List<String> errors;

    // Trace ID for debugging (optional)
    private String traceId;


}
