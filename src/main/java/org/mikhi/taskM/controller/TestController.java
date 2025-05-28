package org.mikhi.taskM.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@Tag(name = "Test Controller", description = "API for testing purposes")
public class TestController {

  @GetMapping("/test")
  @Operation(summary = "Get test message", description = "Returns a simple 'Hello, World!' message")
  public String getTasks() {
    return "Hello, World!";
  }
}