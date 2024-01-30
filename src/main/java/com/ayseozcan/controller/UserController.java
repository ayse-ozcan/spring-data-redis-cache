package com.ayseozcan.controller;

import com.ayseozcan.repository.entity.User;
import com.ayseozcan.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("save/{username}")
    public ResponseEntity<String> saveUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.saveUser(username));
    }

    @GetMapping("find-by-username/{username}")
    public ResponseEntity<Optional<User>> findByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @GetMapping("clear-cache")
    public ResponseEntity<String> clearCache() {
        return ResponseEntity.ok(userService.clearCache());
    }
}
