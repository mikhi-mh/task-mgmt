package org.mikhi.taskM.exception;

public class TaskNotFoundException extends RuntimeException {

  public TaskNotFoundException(String message) {
    super(message);
  }
}
