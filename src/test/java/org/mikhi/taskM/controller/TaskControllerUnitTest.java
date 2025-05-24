package org.mikhi.taskM.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mikhi.taskM.exception.NoTasksFoundException;
import org.mikhi.taskM.exception.TaskNotFoundException;
import org.mikhi.taskM.model.ApiResponseDto;
import org.mikhi.taskM.model.Direction;
import org.mikhi.taskM.model.Status;
import org.mikhi.taskM.model.Task;
import org.mikhi.taskM.service.TaskService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

/**
 * Unit test for {@link TaskController} using {@code  @InjectMocks}.
 * <p>
 * This test class verifies the controller's method logic in isolation. It mocks the
 * {@link TaskService} and directly invokes controller methods without initializing the Spring
 * context or using HTTP simulation.
 */
class TaskControllerUnitTest {

  @Mock
  private TaskService taskService;

  @InjectMocks
  private TaskController taskController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testCreateTask_Success() {
    Task task = Task.builder().title("TestTask").build();
    Task createdTask = Task.builder().id(1L).title("TestTask").build();

    when(taskService.createTask(task)).thenReturn(createdTask);

    ResponseEntity<ApiResponseDto<Task>> response = taskController.createTask(task);

    assertNotNull(response);
    assertEquals(201, response.getStatusCodeValue());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().isSuccess());
    assertEquals("Task with ID 1 created successfully", response.getBody().getMessage());
    assertEquals(1L, response.getBody().getData().getId());
  }

  @Test
  void testGetTaskById_Success() {
    Long taskId = 5L;
    Task mockTask = Task.builder().id(taskId).title("TestTask").status(Status.TODO).build();

    when(taskService.getTaskById(taskId)).thenReturn(mockTask);

    ResponseEntity<ApiResponseDto<Task>> response = taskController.getTaskById(taskId);

    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().isSuccess());
    assertEquals("Task with ID 5 found successfully", response.getBody().getMessage());
    assertEquals(taskId, response.getBody().getData().getId());
    assertEquals("TestTask", response.getBody().getData().getTitle());
    assertEquals(Status.TODO, response.getBody().getData().getStatus());
  }

  @Test
  void testGetTaskById_NotFound() {
    Long taskId = 99L;

    when(taskService.getTaskById(taskId)).thenThrow(
        new TaskNotFoundException("Task not found with id: " + taskId));

    Exception exception = assertThrows(TaskNotFoundException.class,
        () -> taskController.getTaskById(taskId));

    assertEquals("Task not found with id: " + taskId, exception.getMessage());
  }

  @Test
  void testUpdateTask_Success() {
    Long taskId = 2L;
    Task task = Task.builder().title("UpdatedTask").status(Status.IN_PROGRESS).build();
    Task updatedTask = Task.builder().id(taskId).title("UpdatedTask").status(Status.IN_PROGRESS)
        .build();

    when(taskService.updateTask(taskId, task)).thenReturn(updatedTask);

    ResponseEntity<ApiResponseDto<Task>> response = taskController.updateTask(taskId, task);

    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().isSuccess());
    assertEquals("Task with ID 2 updated successfully", response.getBody().getMessage());
    assertEquals(taskId, response.getBody().getData().getId());
    assertEquals("UpdatedTask", response.getBody().getData().getTitle());
    assertEquals(Status.IN_PROGRESS, response.getBody().getData().getStatus());
  }

  @Test
  void testUpdateTask_NotFound() {
    Long taskId = 99L;
    Task task = Task.builder().title("NonExistentTask").build();

    when(taskService.updateTask(taskId, task)).thenThrow(
        new TaskNotFoundException("Task not found with id: " + taskId));

    Exception exception = assertThrows(TaskNotFoundException.class,
        () -> taskController.updateTask(taskId, task));

    assertEquals("Task not found with id: " + taskId, exception.getMessage());
  }


  @Test
  void testDeleteTask_Success() {
    Long taskId = 1L;

    when(taskService.deleteTask(taskId)).thenReturn("Task deleted successfully");

    ResponseEntity<ApiResponseDto<String>> response = taskController.deleteTask(taskId);

    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().isSuccess());
    assertEquals("Task with ID 1 deleted successfully", response.getBody().getMessage());
    assertEquals("Task deleted successfully", response.getBody().getData());
  }

  @Test
  void testDeleteTask_NotFound() {
    Long taskId = 9L;

    when(taskService.deleteTask(taskId)).thenThrow(
        new TaskNotFoundException("Task not found with id: " + taskId));

    Exception exception = assertThrows(TaskNotFoundException.class,
        () -> taskController.deleteTask(taskId));

    assertEquals("Task not found with id: " + taskId, exception.getMessage());
  }

  @Test
  void testGetAllTasks_Success() {
    List<Task> tasks = Arrays.asList(Task.builder().id(1L).build(), Task.builder().id(2L).build());

    when(taskService.getAllTasks()).thenReturn(tasks);

    ResponseEntity<ApiResponseDto<List<Task>>> response = taskController.getAllTasks();

    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().isSuccess());
    assertEquals("Tasks retrieved successfully", response.getBody().getMessage());
    assertEquals(2, response.getBody().getData().size());
  }

  @Test
  void testGetAllTasks_NoTasksFound() {
    when(taskService.getAllTasks()).thenThrow(
        new NoTasksFoundException("No tasks found in the system"));

    Exception exception = assertThrows(NoTasksFoundException.class,
        () -> taskController.getAllTasks());

    assertEquals("No tasks found in the system", exception.getMessage());
  }

  @Test
  void testGetAllTasksPaginated_Success() {

    String sortBy = "dueDate";
    Direction direction = Direction.ASC;
    Sort.Direction sortDirection = Sort.Direction.valueOf(direction.name());
    Pageable pageable = PageRequest.of(0, 5, Sort.by(sortDirection, sortBy));
    List<Task> taskList = Arrays.asList(
        Task.builder().id(1L).title("Task 1").status(Status.TODO).dueDate(LocalDate.of(2025, 10, 1))
            .build(),
        Task.builder().id(2L).title("Task 2").status(Status.IN_PROGRESS)
            .dueDate(LocalDate.of(2024, 10, 5)).build(),
        Task.builder().id(3L).title("Task 3").status(Status.DONE)
            .dueDate(LocalDate.of(2023, 10, 10)).build(),
        Task.builder().id(4L).title("Task 4").status(Status.TODO)
            .dueDate(LocalDate.of(2021, 10, 15)).build(),
        Task.builder().id(5L).title("Task 5").status(Status.IN_PROGRESS)
            .dueDate(LocalDate.of(2020, 10, 20)).build()
    );
    Page<Task> taskPage = new PageImpl<>(taskList, pageable, taskList.size());

    when(taskService.getAllTasks(pageable)).thenReturn(taskPage);
    ResponseEntity<ApiResponseDto<Page<Task>>> allTasksPaginated = taskController.getAllTasksPaginated(
        0, 5,
        sortBy, direction);
    assertNotNull(allTasksPaginated);
    assertEquals(200, allTasksPaginated.getStatusCodeValue());
    assertNotNull(allTasksPaginated.getBody());
    assertTrue(allTasksPaginated.getBody().isSuccess());
    assertEquals("Success", allTasksPaginated.getBody().getMessage());
    assertEquals(5, allTasksPaginated.getBody().getData().getContent().size());
    List<Task> tasks = allTasksPaginated.getBody().getData().getContent();
    tasks.stream().reduce((prev, next) -> {
      assertTrue(prev.getDueDate().isAfter(next.getDueDate()),
          "Task with due date " + prev.getDueDate() + " is not greater than " + next.getDueDate());
      return next;
    });

  }


  @Test
  void testFilterTasks_ByStatus_Success() {
    Status status = Status.TODO;
    List<Task> tasks = Arrays.asList(Task.builder().id(1L).status(status).build());

    when(taskService.filterTasks(status)).thenReturn(tasks);

    ResponseEntity<ApiResponseDto<List<Task>>> response = taskController.filterTasks(status, null);

    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().isSuccess());
    assertEquals("Tasks filtered by status", response.getBody().getMessage());
    assertEquals(1, response.getBody().getData().size());
  }

  @Test
  void testFilterTasks_ByStatus_NoTasksFound() {
    Status status = Status.DONE;

    when(taskService.filterTasks(status)).thenThrow(
        new NoTasksFoundException("No tasks found with status: " + status));

    Exception exception = assertThrows(NoTasksFoundException.class,
        () -> taskController.filterTasks(status, null));

    assertEquals("No tasks found with status: " + status, exception.getMessage());
  }

  @Test
  void testFilterTasks_ByDueDate_Success() {
    LocalDate dueDate = LocalDate.now();
    List<Task> tasks = Arrays.asList(Task.builder().id(1L).dueDate(dueDate).build());

    when(taskService.filterTasks(dueDate)).thenReturn(tasks);

    ResponseEntity<ApiResponseDto<List<Task>>> response = taskController.filterTasks(null, dueDate);

    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().isSuccess());
    assertEquals("Tasks filtered by due date", response.getBody().getMessage());
    assertEquals(1, response.getBody().getData().size());
  }


  @Test
  void testFilterTasks_ByDueDate_NoTasksFound() {
    LocalDate dueDate = LocalDate.now();

    when(taskService.filterTasks(dueDate)).thenThrow(
        new NoTasksFoundException("No tasks found with due date: " + dueDate));

    Exception exception = assertThrows(NoTasksFoundException.class,
        () -> taskController.filterTasks(null, dueDate));

    assertEquals("No tasks found with due date: " + dueDate, exception.getMessage());
  }


  @Test
  void testGetTasksTillDate_Success() {
    LocalDate dueDate = LocalDate.now();
    List<Task> tasks = Arrays.asList(Task.builder().id(1L).dueDate(dueDate).build());

    when(taskService.getTasksTillDate(dueDate)).thenReturn(tasks);

    ResponseEntity<ApiResponseDto<List<Task>>> response = taskController.getTasksTillDate(dueDate);

    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().isSuccess());
    assertEquals("Tasks with due date till " + dueDate + " retrieved successfully",
        response.getBody().getMessage());
    assertEquals(1, response.getBody().getData().size());
  }

  @Test
  void testGetTasksTillDate_NoTasksFound() {
    LocalDate dueDate = LocalDate.now();

    when(taskService.getTasksTillDate(dueDate)).thenThrow(
        new NoTasksFoundException("No tasks found with due date till: " + dueDate));

    Exception exception = assertThrows(NoTasksFoundException.class,
        () -> taskController.getTasksTillDate(dueDate));

    assertEquals("No tasks found with due date till: " + dueDate, exception.getMessage());
  }
}