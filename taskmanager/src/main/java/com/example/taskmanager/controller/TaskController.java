package com.example.taskmanager.controller;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;


    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }


    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return taskRepository.save(task);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if (!taskOpt.isPresent())
            return ResponseEntity.status(404).body("Task not found");

        Task task = taskOpt.get();

        if (taskDetails.getTitle() != null && !taskDetails.getTitle().isEmpty())
            task.setTitle(taskDetails.getTitle());

        if (taskDetails.getDescription() != null)
            task.setDescription(taskDetails.getDescription());

        task.setCompleted(taskDetails.isCompleted());

        taskRepository.save(task);
        return ResponseEntity.ok(task);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        if (!taskRepository.existsById(id))
            return ResponseEntity.status(404).body("Task not found");
        taskRepository.deleteById(id);
        return ResponseEntity.ok("Task deleted");
    }
}

