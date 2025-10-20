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

    /** ✅ Best Sellers toàn shop */
    @GetMapping("/best-sellers")
    public List<HomeProductResponse> getBestSellers(
            @RequestParam(defaultValue = "8") int limit
    ) {
        return homeService.getBestSellers(limit);
    }

    /** ✅ Sản phẩm mới nhất toàn shop */
    @GetMapping("/newest")
    public List<HomeProductResponse> getNewest(
            @RequestParam(defaultValue = "8") int limit
    ) {
        return homeService.getNewestProducts(limit);
    }
}
