package org.mikhi.taskM.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class TestController {

  @GetMapping("/test")
  public String getTasks() {
    return "Hello, World!";
  }

}
