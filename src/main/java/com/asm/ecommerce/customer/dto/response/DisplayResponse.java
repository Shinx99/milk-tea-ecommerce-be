package com.asm.ecommerce.customer.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) //Loai cac truong null ra khoi JSON response -> reponse nhe hon
public class DisplayResponse {
    private UUID id;
    private String email; //Nay la cua feature auth: domain -> user
    private String phone;
    private String fullname;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

}
