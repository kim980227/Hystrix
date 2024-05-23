package com.example.springboot03.controller;

import com.example.springboot03.service.MyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class MyController {

    private final MyService myService;

    @Autowired
    public MyController(MyService myService) {
        this.myService = myService;
    }

    @GetMapping("/example")
    public String exampleEndpoint(Throwable t) {
        System.out.println(t);
        return myService.someServiceCall();
    }
}
