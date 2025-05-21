package org.mikhi.taskM.model;

public enum Direction {
  ASC, DESC;

  public static Direction fromString(String value) {
    if (value == null) {
      throw new IllegalArgumentException("Sort direction cannot be null");
    }
    try {
      return Direction.valueOf(value.toUpperCase());
    } catch (IllegalArgumentException ex) {
      throw new IllegalArgumentException(
          "Invalid sort direction: " + value + ". Allowed values are: asc, desc");
    }
  }
}
