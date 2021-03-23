package com.example.certificationboard.page.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BasicController {

    @GetMapping("/")
    public String index(){
        return "/index";
    }
}
