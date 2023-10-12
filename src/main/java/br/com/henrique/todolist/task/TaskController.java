package br.com.henrique.todolist.task;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    
    private final TaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity<TaskModel> salvar(@RequestBody TaskModel taskModel) {
        TaskModel task = taskRepository.save(taskModel);

        
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }
    
    
}
