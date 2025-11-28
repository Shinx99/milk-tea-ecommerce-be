package com.asm.ecommerce.cart.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RemoveCartRequestDto {

    @NotNull
    private List<UUID> cartItemIds;

}
