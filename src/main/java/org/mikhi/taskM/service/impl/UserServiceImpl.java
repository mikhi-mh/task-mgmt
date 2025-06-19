package org.mikhi.taskM.service.impl;

import org.mikhi.taskM.exception.UserNotFoundException;
import org.mikhi.taskM.model.User;
import org.mikhi.taskM.repository.UserRepository;
import org.mikhi.taskM.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User createUser(User user) {
    return userRepository.save(user);
  }

  @Override
  public User getUserById(int id) {
    return userRepository.findById(id).orElseThrow(
        () -> new UserNotFoundException("User with id " + id + " not found")
    );
  }

  @Override
  public User updateUserById(User user, int id) {
    if (userRepository.existsById(id)) {
      user.setId(id);
      return userRepository.save(user);
    } else {
      throw new UserNotFoundException("User with id " + id + " not found");
    }
  }

  @Override
  public User updateUserByUserName(User user, String userName) {
    if (userRepository.existsUserByUserName(userName)) {
      user.setUserName(userName);
      return userRepository.save(user);
    } else {
      throw new UserNotFoundException("User with userName " + userName + " not found");
    }
  }

  @Override
  public User getUserByUserName(String userName) {
    return userRepository.findByUserName(userName);
  }
}
