package org.mikhi.taskM.controller;

import javax.validation.Valid;
import java.util.List;
import org.mikhi.taskM.model.ApiResponseDto;
import org.mikhi.taskM.model.Task;
import org.mikhi.taskM.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/tasks")
public class TaskController {

  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }


  @PostMapping
  public ResponseEntity<ApiResponseDto<Task>> createTask(@Valid @RequestBody Task task) {
    Task createdTask = taskService.createTask(task);
    ApiResponseDto<Task> response = new ApiResponseDto<>(
        String.format("Task with ID %d created successfully", createdTask.getId()),
        createdTask,
        true
    );
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ApiResponseDto<Task>> getTaskById(@PathVariable Long id) {
    Task task = taskService.getTaskById(id);
    ApiResponseDto<Task> response = new ApiResponseDto<>(
        String.format("Task with ID %d found successfully", id),
        task,
        true
    );
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ApiResponseDto<Task>> updateTask(@PathVariable Long id,
      @Valid @RequestBody Task task) {
    Task updatedTask = taskService.updateTask(id, task);
    ApiResponseDto<Task> response = new ApiResponseDto<>(
        String.format("Task with ID %d updated successfully", id),
        updatedTask,
        true
    );
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponseDto<String>> deleteTask(@PathVariable Long id) {
    String result = taskService.deleteTask(id);
    ApiResponseDto<String> response = new ApiResponseDto<>(
        String.format("Task with ID %d deleted successfully", id),
        result,
        true
    );
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<ApiResponseDto<List<Task>>> getAllTasks() {
    List<Task> tasks = taskService.getAllTasks();
    ApiResponseDto<List<Task>> response = new ApiResponseDto<>(
        "Tasks retrieved successfully",
        tasks,
        true
    );
    return ResponseEntity.ok(response);
  }

}
