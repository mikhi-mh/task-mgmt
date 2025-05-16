package org.mikhi.taskM.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mikhi.taskM.exception.NoTasksFoundException;
import org.mikhi.taskM.exception.TaskNotFoundException;
import org.mikhi.taskM.model.Status;
import org.mikhi.taskM.model.Task;
import org.mikhi.taskM.repository.TaskRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TaskServiceImplTest {

  @Mock
  private TaskRepository taskRepository;

  @InjectMocks
  private TaskServiceImpl taskServiceImpl;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void createTask_success() {
    Task task = new Task();
    when(taskRepository.save(task)).thenReturn(task);
    Task result = taskServiceImpl.createTask(task);
    assertNotNull(result);
  }

  @Test
  void getTaskById_found() {
    Task task = new Task();
    task.setId(1L);
    when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
    Task result = taskServiceImpl.getTaskById(1L);
    assertNotNull(result);
    assertEquals(1L, result.getId());
  }

  @Test
  void getTaskById_notFound() {
    when(taskRepository.findById(22L)).thenReturn(Optional.empty());
    assertThrows(TaskNotFoundException.class, () -> taskServiceImpl.getTaskById(22L));
  }

  @Test
  void updateTask_found() {
    Task task = new Task();
    task.setId(1L);
    task.setTitle("updatedTitle");
    when(taskRepository.existsById(1L)).thenReturn(true);
    when(taskRepository.save(task)).thenReturn(task);
    Task result = taskServiceImpl.updateTask(1L, task);
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("updatedTitle", result.getTitle());
  }

  @Test
  void updateTask_notFound() {
    Task task = new Task();
    when(taskRepository.existsById(2L)).thenReturn(false);
    assertThrows(TaskNotFoundException.class, () -> taskServiceImpl.updateTask(2L, task));
  }

  @Test
  void deleteTask_found() {
    when(taskRepository.existsById(1L)).thenReturn(true);
    doNothing().when(taskRepository).deleteById(1L);
    String result = taskServiceImpl.deleteTask(1L);
    assertEquals("Task deleted successfully", result);
  }

  @Test
  void deleteTask_notFound() {
    when(taskRepository.existsById(2L)).thenReturn(false);
    assertThrows(TaskNotFoundException.class, () -> taskServiceImpl.deleteTask(2L));
  }

  @Test
  void getAllTasks_success() {
    List<Task> tasks = Arrays.asList(new Task(), new Task());
    when(taskRepository.findAll()).thenReturn(tasks);
    List<Task> result = taskServiceImpl.getAllTasks();
    assertEquals(2, result.size());
  }

  @Test
  void getAllTasks_noTasks() {
    when(taskRepository.findAll()).thenReturn(Collections.emptyList());
    assertThrows(NoTasksFoundException.class, () -> taskServiceImpl.getAllTasks());
  }

  @Test
  void filterTasks_byStatus_success() {
    Status appliedFilter = Status.DONE;
    List<Task> tasks = List.of(new Task());
    tasks.forEach(task -> task.setStatus(Status.DONE));
    when(taskRepository.findByStatus(appliedFilter)).thenReturn(tasks);
    List<Task> result = taskServiceImpl.filterTasks(appliedFilter);
    assertEquals(1, result.size());
    tasks.forEach(task -> assertEquals(Status.DONE, task.getStatus()));
  }

  @Test
  void filterTasks_byStatus_noTasks() {
    when(taskRepository.findByStatus(Status.DONE)).thenReturn(Collections.emptyList());
    assertThrows(NoTasksFoundException.class, () -> taskServiceImpl.filterTasks(Status.DONE));
  }

  @Test
  void filterTasks_byDueDate_success() {
    LocalDate date = LocalDate.now();
    List<Task> tasks = List.of(new Task());
    tasks.forEach(task -> task.setDueDate(date));
    when(taskRepository.findByDueDate(date)).thenReturn(tasks);
    List<Task> result = taskServiceImpl.filterTasks(date);
    assertEquals(1, result.size());
    tasks.forEach(task -> assertEquals(date, task.getDueDate()));
  }

  @Test
  void filterTasks_byDueDate_noTasks() {
    LocalDate date = LocalDate.now();
    when(taskRepository.findByDueDate(date)).thenReturn(Collections.emptyList());
    assertThrows(NoTasksFoundException.class, () -> taskServiceImpl.filterTasks(date));
  }

  @Test
  void filterTasks_byStatusAndDueDate_success() {
    LocalDate date = LocalDate.now();
    List<Task> tasks = List.of(new Task());
    tasks.forEach(task -> task.setStatus(Status.TODO));
    tasks.forEach(task -> task.setDueDate(date));
    when(taskRepository.findByStatusAndDueDate(Status.TODO, date)).thenReturn(tasks);
    List<Task> result = taskServiceImpl.filterTasks(Status.TODO, date);
    assertEquals(1, result.size());
    result.forEach(task -> assertEquals(date, task.getDueDate()));
    result.forEach(task -> assertEquals(Status.TODO, task.getStatus()));
  }

  @Test
  void filterTasks_byStatusAndDueDate_noTasks() {
    LocalDate date = LocalDate.now();
    when(taskRepository.findByStatusAndDueDate(Status.DONE, date)).thenReturn(
        Collections.emptyList());
    assertThrows(NoTasksFoundException.class, () -> taskServiceImpl.filterTasks(Status.DONE, date));
  }

  @Test
  void getTasksTillDate_success() {
    LocalDate date = LocalDate.now();
    List<Task> tasks = List.of(new Task());
    tasks.forEach(task -> task.setDueDate(date));
    when(taskRepository.findByDueDateLessThanEqual(date)).thenReturn(tasks);
    List<Task> result = taskServiceImpl.getTasksTillDate(date);
    assertEquals(1, result.size());
    tasks.forEach(
        task -> assertFalse(task.getDueDate().isAfter(date), "Task date should be after date"));
  }
}