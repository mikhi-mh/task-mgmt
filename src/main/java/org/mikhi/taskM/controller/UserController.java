package org.mikhi.taskM.controller;

import org.mikhi.taskM.model.User;
import org.mikhi.taskM.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/")
  public User createUser(@RequestBody User user) {
    return userService.createUser(user);
  }

  @GetMapping("/getUserById/{id}")
  public User getUserById(@PathVariable Integer id) {
    return userService.getUserById(id);
  }

  @GetMapping("/getUserByUserName/{userName}")
  public User getUserById(@PathVariable String userName) {
    return userService.getUserByUserName(userName);
  }

  @PutMapping("/{id}")
  public User updateUserById(@RequestBody User user, @PathVariable Integer id) {
    return userService.updateUserById(user, id);
  }

  @PutMapping("/{userName}")
  public User updateUserByUserName(@RequestBody User user, @PathVariable String userName) {
    return userService.updateUserByUserName(user, userName);
  }


}
