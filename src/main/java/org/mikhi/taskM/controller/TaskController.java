package org.mikhi.taskM.controller;

import io.swagger.v3.oas.annotations.Operation;
import java.time.LocalDate;
import java.util.List;
import javax.validation.Valid;
import org.mikhi.taskM.model.ApiResponseDto;
import org.mikhi.taskM.model.Direction;
import org.mikhi.taskM.model.SortField;
import org.mikhi.taskM.model.Status;
import org.mikhi.taskM.model.Task;
import org.mikhi.taskM.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/tasks")
public class TaskController {

  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @PostMapping
  @Operation(summary = "Create a new task", description = "Creates a new task and returns the created task details")
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
  @Operation(summary = "Get task by ID", description = "Retrieves the details of a task by its ID")
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
  @Operation(summary = "Update a task by ID", description = "Updates the details of an existing task by its ID")
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
  @Operation(summary = "Delete a task by ID", description = "Deletes a task by its ID and returns a confirmation message")
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
  @Operation(summary = "Get all tasks", description = "Retrieves a list of all tasks")
  public ResponseEntity<ApiResponseDto<List<Task>>> getAllTasks() {
    List<Task> tasks = taskService.getAllTasks();
    ApiResponseDto<List<Task>> response = new ApiResponseDto<>(
        "Tasks retrieved successfully",
        tasks,
        true
    );
    return ResponseEntity.ok(response);
  }

  @GetMapping("/paginated")
  @Operation(summary = "Get paginated tasks", description = "Retrieves tasks in a paginated format/page format with sorting options")
  public ResponseEntity<ApiResponseDto<Page<Task>>> getAllTasksPaginated(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "DUE_DATE" ) SortField sortBy,
      @RequestParam(defaultValue = "ASC") Direction direction
  ) {
    if (!SortField.isValid(sortBy.getField())) {
        throw new IllegalArgumentException("Invalid sort field: " + sortBy + ". Allowed fields are: id, title, status, dueDate");
    }
    Sort.Direction sortDirection = Sort.Direction.valueOf(direction.name());
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy.getField()));
    Page<Task> taskPage = taskService.getAllTasks(pageable);
    ApiResponseDto<Page<Task>> response = new ApiResponseDto<>("Success", taskPage, true);
    return ResponseEntity.ok(response);
  }


  @GetMapping("/filter")
  @Operation(summary = "Filter tasks", description = "Filters tasks based on Status and/or Due-date")
  public ResponseEntity<ApiResponseDto<List<Task>>> filterTasks(
      @RequestParam(required = false) Status status,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate) {

    List<Task> filteredTasks;
    String message;

    if (status != null && dueDate != null) {
      filteredTasks = taskService.filterTasks(status, dueDate);
      message = "Tasks filtered by status and due date";
    } else if (status != null) {
      filteredTasks = taskService.filterTasks(status);
      message = "Tasks filtered by status";
    } else if (dueDate != null) {
      filteredTasks = taskService.filterTasks(dueDate);
      message = "Tasks filtered by due date";
    } else {
      filteredTasks = taskService.getAllTasks();
      message = "No filters applied";
    }

    ApiResponseDto<List<Task>> response = new ApiResponseDto<>(
        message,
        filteredTasks,
        true
    );
    return ResponseEntity.ok(response);
  }

  @GetMapping("/till-date")
  @Operation(summary = "Get tasks till a specific date", description = "Retrieves tasks with a due date up to the specified date")
  public ResponseEntity<ApiResponseDto<List<Task>>> getTasksTillDate(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate) {
    List<Task> tasks = taskService.getTasksTillDate(dueDate);
    ApiResponseDto<List<Task>> response = new ApiResponseDto<>(
        "Tasks with due date till " + dueDate + " retrieved successfully",
        tasks,
        true
    );
    return ResponseEntity.ok(response);
  }

}
