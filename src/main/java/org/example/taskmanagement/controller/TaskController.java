package org.example.taskmanagement.controller;

import org.example.taskmanagement.dto.TaskResponseDto;
import org.example.taskmanagement.enums.TaskPriority;
import org.example.taskmanagement.enums.TaskStatus;
import org.example.taskmanagement.model.Task;
import org.example.taskmanagement.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {


    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    //Get all Task
    @GetMapping("/getAllTask")
    public List<TaskResponseDto> getAllTask(){
        return taskService.getAllTask();
    }

    // Get task by Id
    @GetMapping("getTask/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long id){
        return taskService.getTaskById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Create a new task
    @PostMapping("/createTask")
    public TaskResponseDto createTask(@RequestBody Task task, @RequestParam Long userId){
        return taskService.createTask(task, userId);
    }

    //update Task
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable Long id, @RequestBody Task taskDetails){
        return ResponseEntity.ok(taskService.updateTask(id, taskDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    //Assign Task to User
    @PutMapping("/{taskId}/assign/{userId}")
    public  TaskResponseDto assignTaskToUser(@PathVariable Long taskId, @PathVariable Long userId){
        return taskService.assignTaskToUser(taskId,userId);
    }

    //Get Tasks assigned to a User
    @GetMapping("/user/{userId}")
    public List<TaskResponseDto> getUserTasks(@PathVariable Long userId){
        return taskService.findTaskByUserId(userId);
    }

    //Filter task
    @GetMapping("/filter")
    public  List<TaskResponseDto> filterTask(@RequestParam (required = false) TaskStatus status, @RequestParam(required = false) TaskPriority priority){
        return taskService.filterTask(status,priority);
    }
}
