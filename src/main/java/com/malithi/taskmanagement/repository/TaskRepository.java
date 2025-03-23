package com.malithi.taskmanagement.repository;

import com.malithi.taskmanagement.model.Task;
import com.malithi.taskmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user);
    List<Task> findByUserAndStatus(User user, Task.TaskStatus status);
}