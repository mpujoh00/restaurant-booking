package com.restaurant.booking.user.service.controller;

import com.restaurant.booking.user.model.UpdatePasswordRequest;
import com.restaurant.booking.user.model.UpdateRequest;
import com.restaurant.booking.user.model.User;
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
        User user = User.builder().email("micaela@gmail.com").build();

        Mockito.when(authentication.getPrincipal()).thenReturn(user);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(userService.findByEmail("micaela@gmail.com")).thenReturn(user);
        Mockito.when(userService.update(user, updateRequest)).thenReturn(user);

        User obtainedUser = userController.updateUser(updateRequest).getBody();

        Mockito.verify(securityContext).getAuthentication();
        Mockito.verify(authentication).getPrincipal();
        Mockito.verify(userService).findByEmail("micaela@gmail.com");
        Assertions.assertEquals(user, obtainedUser);
    }

    @Test
    void updateUser_notCurrentUser(){
        UpdateRequest updateRequest = new UpdateRequest("another@gmail.com", "contraseña", "Micaela Pujol");
        User user = User.builder().email("micaela@gmail.com").build();

        Mockito.when(authentication.getPrincipal()).thenReturn(user);
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
        Mockito.when(authenticationManager.authenticate(authenticationTokenOld)).thenThrow(new RuntimeException());

        ResponseEntity<String> responseEntity = userController.updateUserPassword(updatePasswordRequest);

        Mockito.verify(securityContext).getAuthentication();
        Mockito.verify(authentication).getPrincipal();
        Assertions.assertEquals("Incorrect password", responseEntity.getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void deleteUser(){
        User user = User.builder().email("micaela@gmail.com").build();

        Mockito.when(authentication.getPrincipal()).thenReturn(user);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(userService.findByEmail("micaela@gmail.com")).thenReturn(user);

        ResponseEntity<Void> responseEntity = userController.deleteUser("micaela@gmail.com");

        Mockito.verify(securityContext).getAuthentication();
        Mockito.verify(authentication).getPrincipal();
        Mockito.verify(userService).findByEmail("micaela@gmail.com");
        Mockito.verify(userService).delete(user);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void deleteUser_notCurrentUser(){
        User user = User.builder().email("micaela@gmail.com").build();

        Mockito.when(authentication.getPrincipal()).thenReturn(user);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        ResponseEntity<Void> responseEntity = userController.deleteUser("another@gmail.com");

        Mockito.verify(securityContext).getAuthentication();
        Mockito.verify(authentication).getPrincipal();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void addRestaurant() {
        User user = User.builder().build();

        Mockito.when(userService.findByEmail("micaela@gmail.com")).thenReturn(user);

        ResponseEntity<Void> responseEntity = userController.addRestaurant("restaurantId", "micaela@gmail.com");

        Mockito.verify(userService).findByEmail("micaela@gmail.com");
        Mockito.verify(userService).addRestaurant(user, "restaurantId");
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void getUsername() {
        User user = User.builder().fullname("micaela").build();

        Mockito.when(userService.findById("id")).thenReturn(user);

        ResponseEntity<String> responseEntity = userController.getUsername("id");

        Mockito.verify(userService).findById("id");
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals("micaela", responseEntity.getBody());
    }
}
