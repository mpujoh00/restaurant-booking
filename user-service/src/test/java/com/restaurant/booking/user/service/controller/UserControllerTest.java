package com.restaurant.booking.user.service.controller;

import com.restaurant.booking.user.model.User;
import com.restaurant.booking.user.service.service.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserControllerImpl userController;

    @Test
    void getAllUsers(){
        List<User> users = List.of(User.builder().email("micaela@gmail.com").build(),
                User.builder().email("diego@gmail.com").build());

        Mockito.when(userService.findAllUsers()).thenReturn(users);

        List<User> obtainedUsers = userController.getAllUsers().getBody();

        Mockito.verify(userService).findAllUsers();
        Assertions.assertEquals(users, obtainedUsers);
    }

    @Test
    void getUserByEmail(){
        User user = User.builder().email("micaela@gmail.com").build();

        Mockito.when(userService.findByEmail("micaela@gmail.com")).thenReturn(user);

        User obtainedUser = userController.getUserByEmail("micaela@gmail.com").getBody();

        Mockito.verify(userService).findByEmail("micaela@gmail.com");
        Assertions.assertEquals(user, obtainedUser);
    }
}
