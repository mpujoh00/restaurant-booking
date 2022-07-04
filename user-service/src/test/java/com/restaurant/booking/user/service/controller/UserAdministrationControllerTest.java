package com.restaurant.booking.user.service.controller;

import com.restaurant.booking.user.model.RegistrationRequest;
import com.restaurant.booking.user.model.User;
import com.restaurant.booking.user.service.service.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class UserAdministrationControllerTest {

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserAdministrationControllerImpl userAdministrationController;

    @Test
    void getAllUsers(){
        List<User> users = List.of(
                User.builder().email("micaela@gmail.com").build(),
                User.builder().email("diego@gmail.com").build());

        Mockito.when(userService.findAllUsers()).thenReturn(users);

        List<User> obtainedUsers = userAdministrationController.getAllUsers().getBody();

        Mockito.verify(userService).findAllUsers();
        Assertions.assertEquals(users, obtainedUsers);
    }

    @Test
    void registerAdmin(){
        RegistrationRequest request = new RegistrationRequest();
        User admin = User.builder().build();

        Mockito.when(userService.registerAdminUser(request)).thenReturn(admin);

        ResponseEntity<User> responseEntity = userAdministrationController.registerAdmin(request);

        Mockito.verify(userService).registerAdminUser(request);
        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Assertions.assertEquals(admin, responseEntity.getBody());
    }

    @Test
    void deleteAdmin(){
        User admin = User.builder().build();

        Mockito.when(userService.findByEmail("email")).thenReturn(admin);

        ResponseEntity<Void> responseEntity = userAdministrationController.deleteAdmin("email");

        Mockito.verify(userService).findByEmail("email");
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void updateUserStatus(){
        User user = User.builder().build();

        Mockito.when(userService.findByEmail("email")).thenReturn(user);
        Mockito.when(userService.changeUserStatus(user)).thenReturn(user);

        ResponseEntity<User> responseEntity = userAdministrationController.updateUserStatus("email");

        Mockito.verify(userService).findByEmail("email");
        Mockito.verify(userService).changeUserStatus(user);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(user, responseEntity.getBody());
    }
}
