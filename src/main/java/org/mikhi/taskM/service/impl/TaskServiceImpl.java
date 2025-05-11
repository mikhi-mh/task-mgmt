package org.mikhi.taskM.service.impl;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import org.mikhi.taskM.exception.NoTasksFoundException;
import org.mikhi.taskM.exception.TaskNotFoundException;
import org.mikhi.taskM.model.Task;
import org.mikhi.taskM.repository.TaskRepository;
import org.mikhi.taskM.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

  private final TaskRepository taskRepository;

  public TaskServiceImpl(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  @Override
  public Task createTask(@Valid @NotNull Task task) {
    return taskRepository.save(task);
  }

  @Override
  public Task getTaskById(@NotNull Long id) {
    return taskRepository.findById(id)
        .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
  }

  @Override
  public Task updateTask(@NotNull Long id, @Valid @NotNull Task task) {
    if (!taskRepository.existsById(id)) {
      throw new TaskNotFoundException("Task not found with id: " + id);
    }
    task.setId(id);
    return taskRepository.save(task);
  }

  @Override
  public String deleteTask(@NotNull Long id) {
    if (!taskRepository.existsById(id)) {
      throw new TaskNotFoundException("Task not found with id: " + id);
    }
    taskRepository.deleteById(id);
    return "Task deleted successfully";
  }

  @Override
  public List<Task> getAllTasks() {
    List<Task> tasks = taskRepository.findAll();
    if (tasks.isEmpty()) {
      throw new NoTasksFoundException("No tasks found in the system");
    }
    return tasks;
  }
}