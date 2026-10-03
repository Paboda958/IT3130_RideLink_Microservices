package com.ridelink.account.controller;

import com.ridelink.account.dto.StatusUpdateRequest;
import com.ridelink.account.dto.UserProfileDto;
import com.ridelink.account.model.Role;
import com.ridelink.account.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@Tag(name = "User Account Management", description = "Endpoints for profile viewing, updating, and account status management")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user account details by ID")
    public ResponseEntity<UserProfileDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get user account details by Email")
    public ResponseEntity<UserProfileDto> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping
    @Operation(summary = "Get all user accounts (Optional filter by role)")
    public ResponseEntity<List<UserProfileDto>> getAllUsers(@RequestParam(required = false) Role role) {
        if (role != null) {
            return ResponseEntity.ok(userService.getUsersByRole(role));
        }
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user account profile information")
    public ResponseEntity<UserProfileDto> updateProfile(@PathVariable Long id, @RequestBody UserProfileDto dto) {
        return ResponseEntity.ok(userService.updateProfile(id, dto));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update account status (ACTIVE, SUSPENDED, INACTIVE)")
    public ResponseEntity<UserProfileDto> updateStatus(@PathVariable Long id, @Valid @RequestBody StatusUpdateRequest request) {
        return ResponseEntity.ok(userService.updateStatus(id, request));
    }
}
