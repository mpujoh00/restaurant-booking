package com.restaurant.booking.user.service.controller;

import com.restaurant.booking.user.model.UpdatePasswordRequest;
import com.restaurant.booking.user.model.UpdateRequest;
import com.restaurant.booking.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "User")
@RequestMapping("/users")
public interface UserController {

    @Operation(description = "Returns a list of all users", operationId = "getAllUsers")
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    ResponseEntity<List<User>> getAllUsers();

    @Operation(description = "Returns a user given its email", operationId = "getUserByEmail")
    @GetMapping("/{email}")
    ResponseEntity<User> getUserByEmail(@PathVariable String email);

    @Operation(description = "Returns the updated user", operationId = "updateUser")
    @PutMapping
    ResponseEntity<User> updateUser(@RequestBody @Valid UpdateRequest updateRequest);

    @Operation(description = "Returns the updated user", operationId = "updateUserPassword")
    @PutMapping("/changePassword")
    ResponseEntity<User> updateUserPassword(@RequestBody @Valid UpdatePasswordRequest updatePasswordRequest);
}
