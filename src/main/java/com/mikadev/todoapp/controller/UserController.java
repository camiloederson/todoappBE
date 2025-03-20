package com.mikadev.todoapp.controller;

import com.mikadev.todoapp.model.User;
import com.mikadev.todoapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/todoapp/users")
public class UserController {

    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista de usuarios registradas en la base de datos")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Obtiene usuario por su id", description = "Obtiene usuario por id")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @Parameter(description = "Id del usuario a traer", example = "1")
            @PathVariable Integer id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(user1 -> ResponseEntity.ok(user1))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    @Operation(summary = "Crear usuarios", description = "Guardar usuarios en la base de datos")
    @PostMapping
    public User saveUser(
            @Parameter(description = "Objeto <User> a almacenar")
            @Valid @RequestBody User user) {
        return userService.saveUser(user);
    }

    @Operation(summary = "Actualizar datos de usuario", description = "Actualizar usuario")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @Parameter(description = "Id del usuario a actualizar")
            @PathVariable Integer id,
            @Parameter(description = "Objeto <User> con datos que seran actualizados")
            @Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @Operation(summary = "Borrar usuarios", description = "Elimina usuarios por su id")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(
            @Parameter(description = "Id del usuario a eliminar")
            @PathVariable Integer id) {
        boolean isDeleted = userService.deleteUserById(id);
        if (isDeleted) return ResponseEntity.ok("Se ha eliminado el usuario");
        else return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("No se ha eliminado el usuario");
    }
}
