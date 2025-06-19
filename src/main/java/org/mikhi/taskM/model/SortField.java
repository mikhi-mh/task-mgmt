package org.mikhi.taskM.model;

import lombok.Getter;

@Getter
public enum SortField {
  ID("id"),
  TITLE("title"),
  STATUS("status"),
  DUE_DATE("dueDate");

  private final String field;

  SortField(String field) {
    this.field = field;
  }

  public static boolean isValid(String sortBy) {
    for (SortField sortField : values()) {
      if (sortField.getField().equalsIgnoreCase(sortBy)) {
        return true;
      }
    }
    return false;
  }
}