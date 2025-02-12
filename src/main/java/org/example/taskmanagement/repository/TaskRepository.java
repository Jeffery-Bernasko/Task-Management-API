package org.example.taskmanagement.repository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.taskmanagement.enums.TaskPriority;
import org.example.taskmanagement.enums.TaskStatus;
import org.example.taskmanagement.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t WHERE " +
            "(:status IS NULL OR t.status = :status) AND " +
            "(:priority IS NULL OR t.priority = :priority)")
    List<Task> filterTask(@Param("status") TaskStatus status, @Param("priority") TaskPriority priority);

    //Fetch task for specific user
    List<Task> findTaskByUserId(Long userId);
}
