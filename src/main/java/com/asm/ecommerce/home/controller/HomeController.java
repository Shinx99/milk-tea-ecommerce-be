// controller/HomeController.java
package com.asm.ecommerce.home.controller;

import com.asm.ecommerce.home.dto.response.HomeProductResponse;
import com.asm.ecommerce.home.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HomeController {

    private final HomeService homeService;

    @GetMapping("/best-sellers")
    public List<HomeProductResponse> getBestSellers(
            @RequestParam(defaultValue = "Milk Tea") String parent,
            @RequestParam(defaultValue = "8") int limit
    ) {
        return homeService.getBestSellers(parent, limit);
    }

    @GetMapping("/newest")
    public List<HomeProductResponse> getNewest(@RequestParam(defaultValue = "8") int limit) {
        return homeService.getNewestProducts(limit);
    }
}