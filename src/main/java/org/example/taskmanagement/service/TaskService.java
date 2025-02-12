package org.example.taskmanagement.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.taskmanagement.enums.TaskPriority;
import org.example.taskmanagement.enums.TaskStatus;
import org.example.taskmanagement.model.Task;
import org.example.taskmanagement.model.User;
import org.example.taskmanagement.repository.TaskRepository;
import org.example.taskmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;



    public List<Task> getAllTask(){
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id){
        return taskRepository.findById(id);
    }

    public Task createTask(Task task){
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task taskDetails){
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException(("Task not found")));
        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setDueDate(taskDetails.getDueDate());
        task.setCompleted(taskDetails.isCompleted());

        //Updates will be happening with the status and the priority
        task.setStatus(taskDetails.getStatus());
        task.setPriority(taskDetails.getPriority());

        return taskRepository.save(task);
    }

    public void deleteTask(Long id){
        taskRepository.deleteById(id);
    }

    // Assign Task to User
    public Task assignTaskToUser(Long taskId, Long userId){
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalTask.isPresent() && optionalUser.isPresent()) {
            Task task = optionalTask.get();
            User user = optionalUser.get();
            task.setUser(user);
            return taskRepository.save(task);
        } else {
            throw new RuntimeException("Task or User not found!");
        }
    }

    // Method to filter task based on status or priority
    public List<Task> filterTask(TaskStatus taskStatus, TaskPriority taskPriority){
        return taskRepository.filterTask(taskStatus,taskPriority);
    }
}


