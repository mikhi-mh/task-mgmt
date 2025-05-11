package org.mikhi.taskM.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import lombok.Data;

@Entity
@Data

public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String title;

  private String description;
  private Status status;

  private LocalDate dueDate;
//  private String createdAt;
//  private String updatedAt;


}
