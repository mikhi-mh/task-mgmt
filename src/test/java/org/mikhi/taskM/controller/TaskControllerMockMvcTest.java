package org.mikhi.taskM.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mikhi.taskM.exception.TaskNotFoundException;
import org.mikhi.taskM.model.Task;
import org.mikhi.taskM.service.TaskService;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * // TaskControllerWebTest Web layer test for {@link TaskController} using {@code @WebMvcTest}.
 * <p>
 * This test verifies controller's request mappings, response statuses, and JSON responses using
 * {@link MockMvc}. The service layer is mocked.
 */
@WebMvcTest(TaskController.class)
class TaskControllerMockMvcTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TaskService taskService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void testCreateTask_Success() throws Exception {
    Task task = Task.builder().title("Test Task").description("Test Description").build();
    Task createdTask = Task.builder().id(1L).title("Test Task").description("Test Description")
        .build();

    Mockito.when(taskService.createTask(Mockito.any(Task.class))).thenReturn(createdTask);

    mockMvc.perform(post("/v1/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(task)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.success", is(true)))
        .andExpect(jsonPath("$.message", containsString("created successfully")))
        .andExpect(jsonPath("$.data.title", is("Test Task")));
  }

  @Test
  void testGetTaskById_Success() throws Exception {
    Task task = Task.builder().id(1L).title("Test Task").build();

    Mockito.when(taskService.getTaskById(1L)).thenReturn(task);

    mockMvc.perform(get("/v1/tasks/{id}", 1L))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success", is(true)))
        .andExpect(jsonPath("$.message", containsString("found successfully")))
        .andExpect(jsonPath("$.data.id", is(1)));
  }

  @Test
  void testGetTaskById_NotFound() throws Exception {
    Mockito.when(taskService.getTaskById(999L))
        .thenThrow(new TaskNotFoundException("Task not found with id: 999"));

    mockMvc.perform(get("/v1/tasks/{id}", 999L))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message", containsString("Task not found with id: 999")));
  }

  @Test
  void testUpdateTask_Success() throws Exception {
    Task updatedTask = Task.builder().id(1L).title("Updated Task").build();

    Mockito.when(taskService.updateTask(Mockito.eq(1L), Mockito.any(Task.class)))
        .thenReturn(updatedTask);

    mockMvc.perform(put("/v1/tasks/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedTask)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success", is(true)))
        .andExpect(jsonPath("$.message", containsString("updated successfully")))
        .andExpect(jsonPath("$.data.title", is("Updated Task")));
  }

  @Test
  void testDeleteTask_Success() throws Exception {
    Mockito.when(taskService.deleteTask(1L)).thenReturn("Task deleted successfully");

    mockMvc.perform(delete("/v1/tasks/{id}", 1L))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success", is(true)))
        .andExpect(jsonPath("$.message", containsString("deleted successfully")));
  }

  @Test
  void testGetAllTasks_Success() throws Exception {
    List<Task> tasks = Arrays.asList(
        Task.builder().id(1L).title("Task 1").build(),
        Task.builder().id(2L).title("Task 2").build()
    );

    Mockito.when(taskService.getAllTasks()).thenReturn(tasks);

    mockMvc.perform(get("/v1/tasks"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success", is(true)))
        .andExpect(jsonPath("$.message", is("Tasks retrieved successfully")))
        .andExpect(jsonPath("$.data", hasSize(tasks.size())));
  }
}