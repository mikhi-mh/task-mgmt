package org.mikhi.taskM.service;

import org.mikhi.taskM.model.User;

public interface UserService {

  public User createUser(User user);

  public User getUserById(int id);

  public User getUserByUserName(String userName);

  public User updateUserById(User user, int id);

  public User updateUserByUserName(User user, String userName);
}