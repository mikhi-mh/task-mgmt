package org.mikhi.taskM.service;


import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.mikhi.taskM.model.Task;

public interface TaskService {

  Task createTask(@Valid @NotNull Task task);

  Task getTaskById(@NotNull Long id);

  Task updateTask(@NotNull Long id, @Valid @NotNull Task task);

  String deleteTask(@NotNull Long id);

  List<Task> getAllTasks();
}