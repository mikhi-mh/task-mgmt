package org.mikhi.taskM.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  @GetMapping({
      "/",
      "/home",
      "/index",
      "/swagger",
      "/home.html",
      "/index.html",
      "/swagger-ui",
      "/swagger.html"
  })
  public String redirectToSwagger() {
    return "redirect:/swagger-ui/index.html";
  }
}
