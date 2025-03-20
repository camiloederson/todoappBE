package com.mikadev.todoapp.controller;

import com.mikadev.todoapp.model.Task;
import com.mikadev.todoapp.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/todoapp/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Operation(summary = "Obtener todas las tareas", description = "Devuelve una lista de tareas registradas en la base de datos")
    @GetMapping
    public List<Task> getAllTasks (){
        return taskService.getAllTasks();
    }

    @Operation(summary = "Obtener una tarea por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tarea encontrada"),
            @ApiResponse(responseCode = "404", description = "Tarea no encontrada. Lanza un error para manejarse en el frontend")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Integer id){
        Optional<Task> task = taskService.getTaskById(id);
        return task.map(ResponseEntity::ok).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarea no encontrada"));
    }

    @Operation(summary = "Crea una tarea en la base de datos", description = "Devuelve el objeto creado")
    @PostMapping
    public ResponseEntity<Task> saveTask (@Valid @RequestBody Task task) throws Exception {
        Task savedTask = taskService.saveTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
    }

    @Operation(summary = "Actualiza una tarea. Recibe id y el objeto a actualizar", description = "Devuelve 200 Si la tarea es creada y el objeto tarea")
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @Parameter(description = "Id de la tarea a actualizar")
            @PathVariable Integer id,
            @Parameter(description = "Objeto <Task> a actualizar.")
            @Valid @RequestBody Task task){

        Task taskUpdated = taskService.updateTask(id, task);
        return ResponseEntity.ok(taskUpdated);
    }
@Operation(summary = "Elimina una tarea", description = "Elimina una tarea. Devuelve 200 y un string de confirmacion. Si no es eliminado se envia Not Acceptable")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTaskById(
            @Parameter(description = "Id de la tarea. Integer", example = "1")
            @PathVariable Integer id){
        boolean deleted = taskService.deleteTareaById(id);
        if (deleted){
            return ResponseEntity.noContent().build();
        }
        else return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("No se elimino la tarea");
    }
}
