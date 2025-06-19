package org.mikhi.taskM.repository;

import org.mikhi.taskM.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  boolean existsUserByUserName(String userName);

  User findByUserName(String userName);
}