package com.asm.ecommerce.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProductController {

    @RequestMapping("/hello/sayhi")
    public String sayHi(Model model){
        model.addAttribute("sayhy","hi hi hi heehehehihihihaahahahhaha hahahhahhahahah hihihihi!");
        model.addAttribute("subject","Spring Boot MVC - test connection Acc");
        return "hello";
    }
}
