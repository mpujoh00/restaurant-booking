package com.restaurant.booking.user.service.controller;

import com.restaurant.booking.user.model.RegistrationRequest;
import com.restaurant.booking.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "UserAdministration")
//@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RequestMapping("/api/v1/admin/users")
public interface UserAdministrationController {

    @Operation(description = "Returns a list of all users", operationId = "getAllUsers")
    @GetMapping
    ResponseEntity<List<User>> getAllUsers();

    @Operation(description = "Creates a new user with admin role", operationId = "registerAdmin")
    @PostMapping
    ResponseEntity<User> registerAdmin(@RequestBody @Valid RegistrationRequest registrationRequest);

    @Operation(description = "Deletes a user with admin role", operationId = "registerAdmin")
    @DeleteMapping("/delete/{email}")
    ResponseEntity<Void> deleteAdmin(@PathVariable String email);

    @Operation(description = "Updates the status of a user either to disabled or enabled", operationId = "updateUserStatus")
    @PutMapping("/change-status/{email}")
    ResponseEntity<User> updateUserStatus(@PathVariable String email);
}
