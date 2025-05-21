package org.mikhi.taskM.service;


import java.time.LocalDate;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.mikhi.taskM.model.Status;
import org.mikhi.taskM.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface TaskService {

  Task createTask(@Valid @NotNull Task task);

  Task getTaskById(@NotNull Long id);

  Task updateTask(@NotNull Long id, @Valid @NotNull Task task);

  String deleteTask(@NotNull Long id);

  List<Task> getAllTasks();

  Page<Task> getAllTasks(Pageable pageable);

  List<Task> filterTasks(Status status);  // Filter by status

  List<Task> filterTasks(LocalDate dueDate);  // Filter by dueDate

  List<Task> filterTasks(Status status, LocalDate dueDate);  // Filter by both

  List<Task> getTasksTillDate(LocalDate dueDate);
}