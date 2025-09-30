package com.Demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class HomeController {

    @GetMapping("/get")
    public String hello(){
        return "hello Students";
    }


}
