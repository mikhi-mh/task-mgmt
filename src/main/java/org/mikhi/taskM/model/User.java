package org.mikhi.taskM.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {

  @GeneratedValue(strategy = GenerationType.AUTO)
  @Id
  private Integer id;

  private String userName;

  private String fullName;

}