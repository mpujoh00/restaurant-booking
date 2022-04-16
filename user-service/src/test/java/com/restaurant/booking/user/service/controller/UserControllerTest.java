package com.restaurant.booking.user.service.controller;

import com.restaurant.booking.user.model.UpdatePasswordRequest;
import com.restaurant.booking.user.model.UpdateRequest;
import com.restaurant.booking.user.model.User;
import com.restaurant.booking.user.service.exception.UserNotFoundException;
import com.restaurant.booking.user.service.security.JwtUtils;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserControllerImpl userController;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Test
    void getAllUsers(){
        List<User> users = List.of(
                User.builder().email("micaela@gmail.com").build(),
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

    @Test
    void updateUser(){
        UpdateRequest updateRequest = new UpdateRequest("micaela@gmail.com", "contraseña", "Micaela");
        User userUnmodified = User.builder().email("micaela@gmail.com").build();
        User userModified = User.builder().email("micaela@gmail.com").password("contraseña").fullname("Micaela").build();

        Mockito.when(authentication.getPrincipal()).thenReturn("micaela@gmail.com");
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(userService.findByEmail("micaela@gmail.com")).thenReturn(userUnmodified);
        Mockito.when(userService.update(userModified)).thenReturn(userModified);

        User obtainedUser = userController.updateUser(updateRequest).getBody();

        Mockito.verify(securityContext).getAuthentication();
        Mockito.verify(authentication).getPrincipal();
        Mockito.verify(userService).findByEmail("micaela@gmail.com");
        Assertions.assertEquals(userModified, obtainedUser);
    }

    @Test
    void updateUser_notCurrentUser(){
        UpdateRequest updateRequest = new UpdateRequest("another@gmail.com", "contraseña", "Micaela Pujol");

        Mockito.when(authentication.getPrincipal()).thenReturn("micaela@gmail.com");
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        ResponseEntity<User> responseEntity = userController.updateUser(updateRequest);

        Mockito.verify(securityContext).getAuthentication();
        Mockito.verify(authentication).getPrincipal();
        Assertions.assertNull(responseEntity.getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void updateUserPassword(){
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest("contraseña vieja", "contraseña nueva");
        UsernamePasswordAuthenticationToken authenticationTokenOld =
                new UsernamePasswordAuthenticationToken("micaela@gmail.com", "contraseña vieja");
        UsernamePasswordAuthenticationToken authenticationTokenNew =
                new UsernamePasswordAuthenticationToken("micaela@gmail.com", "contraseña nueva");
        User userOld = User.builder().email("micaela@gmail.com").password("contraseña vieja").build();
        User userNew = User.builder().email("micaela@gmail.com").password("contraseña nueva").build();

        Mockito.when(authentication.getPrincipal()).thenReturn("micaela@gmail.com");
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(authenticationManager.authenticate(authenticationTokenOld)).thenReturn(authentication);
        Mockito.when(userService.findByEmail("micaela@gmail.com")).thenReturn(userOld);
        Mockito.when(authenticationManager.authenticate(authenticationTokenNew)).thenReturn(authentication);
        Mockito.when(jwtUtils.generateJwtToken(authentication)).thenReturn("NEW-JWT-TOKEN");

        String obtainedToken = userController.updateUserPassword(updatePasswordRequest).getBody();

        Mockito.verify(securityContext).getAuthentication();
        Mockito.verify(authentication).getPrincipal();
        Mockito.verify(authenticationManager).authenticate(authenticationTokenOld);
        Mockito.verify(userService).findByEmail("micaela@gmail.com");
        Mockito.verify(userService).save(userNew);
        Mockito.verify(authenticationManager).authenticate(authenticationTokenNew);
        Mockito.verify(securityContext).setAuthentication(authentication);
        Assertions.assertEquals("NEW-JWT-TOKEN", obtainedToken);
    }

    @Test
    void updateUserPassword_incorrectPassword(){
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest("contraseña vieja mal", "contraseña nueva");
        UsernamePasswordAuthenticationToken authenticationTokenOld =
                new UsernamePasswordAuthenticationToken("micaela@gmail.com", "contraseña vieja mal");

        Mockito.when(authentication.getPrincipal()).thenReturn("micaela@gmail.com");
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        ResponseEntity<String> responseEntity = userController.updateUserPassword(updatePasswordRequest);

        Mockito.verify(securityContext).getAuthentication();
        Mockito.verify(authentication).getPrincipal();
        Mockito.verify(authenticationManager).authenticate(authenticationTokenOld);
        Assertions.assertEquals("Incorrect password", responseEntity.getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void deleteUser(){
        Mockito.when(authentication.getPrincipal()).thenReturn("micaela@gmail.com");
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(userService.delete("micaela@gmail.com")).thenReturn(null);

        ResponseEntity<Void> responseEntity = userController.deleteUser("micaela@gmail.com");

        Mockito.verify(securityContext).getAuthentication();
        Mockito.verify(authentication).getPrincipal();
        Mockito.verify(userService).delete("micaela@gmail.com");
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void deleteUser_notCurrentUser(){
        Mockito.when(authentication.getPrincipal()).thenReturn("micaela@gmail.com");
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        ResponseEntity<Void> responseEntity = userController.deleteUser("another@gmail.com");

        Mockito.verify(securityContext).getAuthentication();
        Mockito.verify(authentication).getPrincipal();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void deleteUser_notFound(){
        Mockito.when(authentication.getPrincipal()).thenReturn("micaela@gmail.com");
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(userService.delete("micaela@gmail.com")).thenThrow(new UserNotFoundException(""));

        ResponseEntity<Void> responseEntity = userController.deleteUser("micaela@gmail.com");

        Mockito.verify(securityContext).getAuthentication();
        Mockito.verify(authentication).getPrincipal();
        Mockito.verify(userService).delete("micaela@gmail.com");
        Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void isNotCurrentUser(){
        Mockito.when(authentication.getPrincipal()).thenReturn("micaela@gmail.com");
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        boolean isNotCurrentUser = userController.isNotCurrentUser("another@gmail.com");

        Mockito.verify(securityContext).getAuthentication();
        Mockito.verify(authentication).getPrincipal();
        Assertions.assertTrue(isNotCurrentUser);

    }
}
