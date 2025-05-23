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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mikhi.taskM.model.Status;
import org.mikhi.taskM.model.Task;
import org.mikhi.taskM.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private TaskRepository taskRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    taskRepository.deleteAll();
  }

  @Test
  void testCreateTask_Success() throws Exception {
    Task task = Task.builder().title("Test Task").description("Test Description").build();

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
    Task task = taskRepository.save(Task.builder().title("Test Task").build());

    mockMvc.perform(get("/v1/tasks/{id}", task.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success", is(true)))
        .andExpect(jsonPath("$.message", containsString("found successfully")))
        .andExpect(jsonPath("$.data.id", is(task.getId().intValue())));
  }

  @Test
  void testGetTaskById_NotFound() throws Exception {
    mockMvc.perform(get("/v1/tasks/{id}", 999))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is(404)))
        .andExpect(jsonPath("$.message", containsString("Task not found with id: 999")));
  }

  @Test
  void testUpdateTask_Success() throws Exception {
    Task task = taskRepository.save(Task.builder().title("Old Task").build());
    Task updatedTask = Task.builder().title("Updated Task").build();

    mockMvc.perform(put("/v1/tasks/{id}", task.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedTask)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success", is(true)))
        .andExpect(jsonPath("$.message", containsString("updated successfully")))
        .andExpect(jsonPath("$.data.title", is("Updated Task")));
  }

  @Test
  void testUpdateTask_NotFound() throws Exception {
    Task updatedTask = Task.builder().title("NonExistentTask").build();

    mockMvc.perform(put("/v1/tasks/{id}", 999)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedTask)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is(404)))
        .andExpect(jsonPath("$.message", containsString("Task not found with id: 999")));
  }

  @Test
  void testDeleteTask_Success() throws Exception {
    Task task = taskRepository.save(Task.builder().title("Task to Delete").build());

    mockMvc.perform(delete("/v1/tasks/{id}", task.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success", is(true)))
        .andExpect(jsonPath("$.message", containsString("deleted successfully")));
  }

  @Test
  void testDeleteTask_NotFound() throws Exception {
    mockMvc.perform(delete("/v1/tasks/{id}", 999))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is(404)))
        .andExpect(jsonPath("$.message", containsString("Task not found with id: 999")));
  }

  @Test
  void testGetAllTasks_Success() throws Exception {
    List<Task> tasks = Arrays.asList(
        Task.builder().title("Task 1").build(),
        Task.builder().title("Task 2").build()
    );
    taskRepository.saveAll(tasks);

    mockMvc.perform(get("/v1/tasks"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success", is(true)))
        .andExpect(jsonPath("$.message", is("Tasks retrieved successfully")))
        .andExpect(jsonPath("$.data", hasSize(2)));
  }

  @Test
  void testGetAllTasks_NoTasksFound() throws Exception {
    mockMvc.perform(get("/v1/tasks"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is(404)))
        .andExpect(jsonPath("$.message", is("No tasks found in the system")));
  }

  @Test
  void testFilterTasks_ByStatus_Success() throws Exception {
    Task task = taskRepository.save(Task.builder().title("Task 1").status(Status.TODO).build());

    mockMvc.perform(get("/v1/tasks/filter").param("status", "TODO"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success", is(true)))
        .andExpect(jsonPath("$.message", is("Tasks filtered by status")))
        .andExpect(jsonPath("$.data[0].status", is("TODO")));
  }

  @Test
  void testFilterTasks_ByStatus_NoTasksFound() throws Exception {
    mockMvc.perform(get("/v1/tasks/filter").param("status", "DONE"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is(404)))
        .andExpect(jsonPath("$.message", containsString("No tasks found with status: DONE")));
  }

  @Test
  void testFilterTasks_ByDueDate_NoTasksFound() throws Exception {
    LocalDate dueDate = LocalDate.now();

    mockMvc.perform(get("/v1/tasks/filter").param("dueDate", dueDate.toString()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is(404)))
        .andExpect(
            jsonPath("$.message", containsString("No tasks found with due date: " + dueDate)));
  }

  @Test
  void testGetTasksTillDate_Success() throws Exception {
    LocalDate dueDate = LocalDate.now();
    Task task = taskRepository.save(Task.builder().title("Task 1").dueDate(dueDate).build());

    mockMvc.perform(get("/v1/tasks/till-date").param("dueDate", dueDate.toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success", is(true)))
        .andExpect(jsonPath("$.message", containsString("retrieved successfully")))
        .andExpect(jsonPath("$.data[0].dueDate", is(dueDate.toString())));
  }

  @Test
  void testGetTasksTillDate_NoTasksFound() throws Exception {
    LocalDate dueDate = LocalDate.now();

    mockMvc.perform(get("/v1/tasks/till-date").param("dueDate", dueDate.toString()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is(404)))
        .andExpect(
            jsonPath("$.message", containsString("No tasks found with due date till: " + dueDate)));
  }


}