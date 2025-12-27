package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll();
    }

    @PatchMapping("/{id}/activation")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setActivation(@PathVariable Long id, @RequestParam("active") boolean active) {
        userService.setActive(id, active);
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody UserDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(dto));
    }

    @PutMapping("/{id}")
    public UserDto update(@PathVariable Long id, @RequestBody UserDto dto) {
        return userService.update(id, dto);
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Void> exists(@PathVariable Long id) {
        userService.checkExistsAndActive(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/name")
    public String getName(@PathVariable Long id) {
        return userService.getUserFullName(id);
    }
}


