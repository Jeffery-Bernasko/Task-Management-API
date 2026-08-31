package org.example.taskmanagement.service;

import lombok.RequiredArgsConstructor;
import org.example.taskmanagement.dto.TaskResponseDto;
import org.example.taskmanagement.dto.UserResponseDto;
import org.example.taskmanagement.enums.TaskPriority;
import org.example.taskmanagement.enums.TaskStatus;
import org.example.taskmanagement.model.Task;
import org.example.taskmanagement.model.User;
import org.example.taskmanagement.repository.TaskRepository;
import org.example.taskmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;



    public List<TaskResponseDto> getAllTask(){
        return taskRepository.findAll()
                .stream()
                .map(this::mapToTaskResponseDto)
                .collect(Collectors.toList());
    }

    public Optional<TaskResponseDto> getTaskById(Long id){
        return taskRepository.findById(id).map(this::mapToTaskResponseDto);
    }

    public TaskResponseDto createTask(Task task, Long userId){
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
        task.setUser(user);
        return mapToTaskResponseDto(taskRepository.save(task));
    }

    public TaskResponseDto updateTask(Long id, Task taskDetails){
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException(("Task not found")));
        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setDueDate(taskDetails.getDueDate());
        task.setCompleted(taskDetails.isCompleted());

        //Updates will be happening with the status and the priority
        task.setStatus(taskDetails.getStatus());
        task.setPriority(taskDetails.getPriority());

        return mapToTaskResponseDto(taskRepository.save(task));
    }

    public void deleteTask(Long id){
        taskRepository.deleteById(id);
    }

    // Assign Task to User
    public TaskResponseDto assignTaskToUser(Long taskId, Long userId){
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalTask.isPresent() && optionalUser.isPresent()) {
            Task task = optionalTask.get();
            User user = optionalUser.get();
            task.setUser(user);
            return mapToTaskResponseDto(taskRepository.save(task));
        } else {
            throw new RuntimeException("Task or User not found!");
        }
    }

    // Method to filter task based on status or priority
    public List<TaskResponseDto> filterTask(TaskStatus taskStatus, TaskPriority taskPriority){
        return taskRepository.filterTask(taskStatus,taskPriority)
                .stream()
                .map(this::mapToTaskResponseDto)
                .collect(Collectors.toList());
    }

    //Fetch tasks for specific user
     public List<TaskResponseDto> findTaskByUserId(Long userId){
        return taskRepository.findTaskByUserId(userId)
                .stream()
                .map(this::mapToTaskResponseDto)
                .collect(Collectors.toList());
    }

    private TaskResponseDto mapToTaskResponseDto(Task task) {
        return new TaskResponseDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.isCompleted(),
                task.getStatus(),
                task.getPriority(),
                mapToUserResponseDto(task.getUser())
        );
    }

    private UserResponseDto mapToUserResponseDto(User user) {
        if (user == null) {
            return null;
        }

        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}


