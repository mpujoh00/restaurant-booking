package com.restaurant.booking.user.service.controller;

import com.restaurant.booking.user.model.*;
import com.restaurant.booking.user.service.exception.UserLoginDisabledException;
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

import java.util.Set;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private UserServiceImpl userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthenticationControllerImpl authenticationController;

    @Test
    void login(){
        LoginRequest request = new LoginRequest("micaela@gmail.com", "1234");
        UsernamePasswordAuthenticationToken userPassAuthToken =
                new UsernamePasswordAuthenticationToken("micaela@gmail.com", "1234");
        User user = User.builder().email("micaela@gmail.com").status(UserStatus.ENABLED).build();

        Mockito.when(userService.findByEmail("micaela@gmail.com")).thenReturn(user);
        Mockito.when(authenticationManager.authenticate(userPassAuthToken)).thenReturn(authentication);
        Mockito.when(jwtUtils.generateJwtToken(authentication)).thenReturn("JWT-TOKEN");

        ResponseEntity<LoginResponse> responseEntity = authenticationController.login(request);

        Mockito.verify(authenticationManager).authenticate(userPassAuthToken);
        Mockito.verify(jwtUtils).generateJwtToken(authentication);

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getBody());
        Assertions.assertEquals("JWT-TOKEN", responseEntity.getBody().getToken());
    }

    @Test
    void login_disabledUser(){
        LoginRequest request = new LoginRequest("micaela@gmail.com", "1234");
        User user = User.builder().email("micaela@gmail.com").status(UserStatus.DISABLED).build();

        Mockito.when(userService.findByEmail("micaela@gmail.com")).thenReturn(user);

        UserLoginDisabledException exception = Assertions.assertThrows(
                UserLoginDisabledException.class, () -> authenticationController.login(request));
        Assertions.assertEquals("Can't login, user micaela@gmail.com is disabled", exception.getMessage());
    }

    @Test
    void register(){
        User user = User.builder().email("micaela@gmail.com").password("1234")
                .fullname("micaela").roles(Set.of(new Role(RoleName.ROLE_ADMIN))).build();
        RegistrationRequest request = new RegistrationRequest
                (user.getEmail(), user.getPassword(), user.getFullname(), RoleName.ROLE_ADMIN);

        Mockito.when(userService.registerBaseUser(request)).thenReturn(user);

        ResponseEntity<User> responseEntity = authenticationController.register(request);

        Mockito.verify(userService).registerBaseUser(request);

        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity.getBody());
        Assertions.assertEquals(user, responseEntity.getBody());
    }
}
