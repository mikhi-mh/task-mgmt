package org.mikhi.taskM.repository;

import java.time.LocalDate;
import java.util.List;
import org.mikhi.taskM.model.Status;
import org.mikhi.taskM.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

  Page<Task> findAll(Pageable pageable);

  List<Task> findByStatus(Status status);

  List<Task> findByDueDate(LocalDate dueDate);

  List<Task> findByStatusAndDueDate(Status status, LocalDate dueDate);

  List<Task> findByDueDateLessThanEqual(LocalDate dueDateIsLessThan);

}
