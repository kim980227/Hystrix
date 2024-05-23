package com.example.springboot03;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Index {
  @RequestMapping("/")
  public String func01() {
    return "index";
  }

  @RequestMapping("/tiger")
  public String func02() {
    return "tiger";
  }

}
