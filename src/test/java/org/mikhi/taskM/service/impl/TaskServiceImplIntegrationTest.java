package org.mikhi.taskM.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mikhi.taskM.exception.NoTasksFoundException;
import org.mikhi.taskM.exception.TaskNotFoundException;
import org.mikhi.taskM.model.Status;
import org.mikhi.taskM.model.Task;
import org.mikhi.taskM.repository.TaskRepository;
import org.mikhi.taskM.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TaskServiceImplIntegrationTest {

  @Autowired
  private TaskService taskService;

  @Autowired
  private TaskRepository taskRepository;

  private Task createTestTask(String title) {
    Task task = new Task();
    task.setTitle(title);
    task.setDescription("Test Description");
    return task;
  }

  @BeforeEach
  void cleanup() {
    taskRepository.deleteAll();
  }

  @Test
  void createTask_success() {
    Task task = createTestTask("TestTask");
    Task result = taskService.createTask(task);

    assertNotNull(result);
    assertNotNull(result.getId());
    assertTrue(taskRepository.findById(result.getId()).isPresent());
  }

  @Test
  void getTaskById_found() {
    Task savedTask = taskRepository.save(createTestTask("TestTask"));
    Task result = taskService.getTaskById(savedTask.getId());

    assertNotNull(result);
    assertEquals(savedTask.getId(), result.getId());
    assertEquals(savedTask.getTitle(), result.getTitle());
  }

  @Test
  void getTaskById_notFound() {
    assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(999L));
  }

  @Test
  void updateTask_success() {
    Task savedTask = taskRepository.save(createTestTask("OriginalTitle"));
    Task updateTask = createTestTask("UpdatedTitle");

    Task result = taskService.updateTask(savedTask.getId(), updateTask);

    assertNotNull(result);
    assertEquals("UpdatedTitle", result.getTitle());
  }

  @Test
  void updateTask_notFound() {
    Task updateTask = createTestTask("TestTask");
    assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(999L, updateTask));
  }

  @Test
  void deleteTask_success() {
    Task savedTask = taskRepository.save(createTestTask("TestTask"));
    String result = taskService.deleteTask(savedTask.getId());

    assertEquals("Task deleted successfully", result);
    assertFalse(taskRepository.existsById(savedTask.getId()));
  }

  @Test
  void deleteTask_notFound() {
    assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(999L));
  }

  @Test
  void getAllTasks_success() {
    taskRepository.save(createTestTask("Task1"));
    taskRepository.save(createTestTask("Task2"));

    List<Task> result = taskService.getAllTasks();

    assertEquals(2, result.size());
    assertTrue(result.stream().anyMatch(t -> t.getTitle().equals("Task1")));
    assertTrue(result.stream().anyMatch(t -> t.getTitle().equals("Task2")));
  }


  @Test
  void filterTasks_byStatus_success() {
    Task task = createTestTask("TaskStatus");
    task.setStatus(Status.DONE);
    taskRepository.save(task);

    List<Task> result = taskService.filterTasks(Status.DONE);

    assertEquals(1, result.size());
    assertEquals(Status.DONE, result.get(0).getStatus());
  }

  @Test
  void filterTasks_byStatus_noTasks() {
    assertThrows(NoTasksFoundException.class, () -> taskService.filterTasks(Status.DONE));
  }

  @Test
  void filterTasks_byDueDate_success() {
    LocalDate date = LocalDate.now();
    Task task = createTestTask("TaskDueDate");
    task.setDueDate(date);
    taskRepository.save(task);

    List<Task> result = taskService.filterTasks(date);

    assertEquals(1, result.size());
    assertEquals(date, result.get(0).getDueDate());
  }

  @Test
  void filterTasks_byDueDate_noTasks() {
    LocalDate date = LocalDate.now();
    assertThrows(NoTasksFoundException.class, () -> taskService.filterTasks(date));
  }

  @Test
  void filterTasks_byStatusAndDueDate_success() {
    LocalDate date = LocalDate.now();
    Task task = createTestTask("TaskStatusDueDate");
    task.setStatus(Status.TODO);
    task.setDueDate(date);
    taskRepository.save(task);

    List<Task> result = taskService.filterTasks(Status.TODO, date);

    assertEquals(1, result.size());
    assertEquals(Status.TODO, result.get(0).getStatus());
    assertEquals(date, result.get(0).getDueDate());
  }

  @Test
  void filterTasks_byStatusAndDueDate_noTasks() {
    LocalDate date = LocalDate.now();
    assertThrows(NoTasksFoundException.class, () -> taskService.filterTasks(Status.DONE, date));
  }

  @Test
  void getTasksTillDate_success() {
    LocalDate date = LocalDate.now();
    Task task1 = createTestTask("Task1");
    task1.setDueDate(date.minusDays(1));
    Task task2 = createTestTask("Task2");
    task2.setDueDate(date);
    taskRepository.save(task1);
    taskRepository.save(task2);

    List<Task> result = taskService.getTasksTillDate(date);

    assertEquals(2, result.size());
    assertTrue(result.stream().noneMatch(t -> t.getDueDate().isAfter(date)));
  }
}