package com.restaurant.booking.user.service.controller;

import com.restaurant.booking.user.model.UpdatePasswordRequest;
import com.restaurant.booking.user.model.UpdateRequest;
import com.restaurant.booking.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "User")
@RequestMapping("/api/v1/users")
public interface UserController {

    @Operation(description = "Returns a user given its email", operationId = "getUserByEmail")
    @GetMapping("/{email}")
    ResponseEntity<User> getUserByEmail(@PathVariable String email);

    @Operation(description = "Returns the updated user", operationId = "updateUser")
    @PutMapping
    ResponseEntity<User> updateUser(@RequestBody @Valid UpdateRequest updateRequest);

    @Operation(description = "Returns the new token for the updated user", operationId = "updateUserPassword")
    @PutMapping("/change-password")
    ResponseEntity<String> updateUserPassword(@RequestBody @Valid UpdatePasswordRequest updatePasswordRequest);

    @Operation(description = "Deletes an user", operationId = "deleteUser")
    @DeleteMapping("/delete/{email}")
    ResponseEntity<Void> deleteUser(@PathVariable String email);

    @Operation(description = "Adds the given restaurant to the user", operationId = "addRestaurant")
    @PutMapping("/add-restaurant/{restaurantId}/user/{userEmail}")
    ResponseEntity<Void> addRestaurant(@PathVariable String restaurantId, @PathVariable String userEmail);

    @Operation(description = "Returns a user's name given its id", operationId = "getUsername")
    @GetMapping("/username/{id}")
    ResponseEntity<String> getUsername(@PathVariable String id);
}
