package com.restaurant.booking.user.service.service;

import com.restaurant.booking.user.model.RegistrationRequest;
import com.restaurant.booking.user.model.Role;
import com.restaurant.booking.user.model.RoleName;
import com.restaurant.booking.user.model.User;
import com.restaurant.booking.user.service.exception.RoleNotFoundException;
import com.restaurant.booking.user.service.exception.UserAlreadyExistsException;
import com.restaurant.booking.user.service.exception.UserNotFoundException;
import com.restaurant.booking.user.service.repository.RoleRepository;
import com.restaurant.booking.user.service.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findAllUsers(){
        List<User> users = List.of(
                User.builder().email("micaela@gmail.com").build(),
                User.builder().email("pepe@gmail.com").build());

        Mockito.when(userRepository.findAll()).thenReturn(users);

        List<User> obtainedUsers = userService.findAllUsers();

        Mockito.verify(userRepository).findAll();
        Assertions.assertEquals(users, obtainedUsers);
    }

    @Test
    void findByEmail(){
        User user = User.builder().email("micaela@gmail.com").build();

        // specifies what the mock userRepository has to return when a method (and args) is called
        Mockito.when(userRepository.findByEmail("micaela@gmail.com")).thenReturn(Optional.of(user));

        User result = userService.findByEmail("micaela@gmail.com");

        // checks if the method was executed
        Mockito.verify(userRepository).findByEmail("micaela@gmail.com");
        // compares expected output with actual output
        Assertions.assertEquals("micaela@gmail.com", result.getEmail());
    }

    @Test
    void findByEmail_userNotFound(){
        Mockito.when(userRepository.findByEmail("micaela@gmail.com")).thenReturn(Optional.empty());

        UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class, () -> userService.findByEmail("micaela@gmail.com"));
        Assertions.assertEquals("User with email: micaela@gmail.com not found", exception.getMessage());

        Mockito.verify(userRepository).findByEmail("micaela@gmail.com");
    }

    @Test
    void saveUser(){
        User user = User.builder().email("micaela@gmail.com").password("1234").roles(Set.of(new Role(RoleName.ROLE_ADMIN))).build();
        User expected = User.builder().email("micaela@gmail.com").password("encoded").roles(Set.of(new Role(RoleName.ROLE_ADMIN))).build();

        Mockito.when(bCryptPasswordEncoder.encode("1234")).thenReturn("encoded");
        Mockito.when(userRepository.save(expected)).then(returnsFirstArg());

        User result = userService.save(user);

        Mockito.verify(bCryptPasswordEncoder).encode("1234");
        Mockito.verify(userRepository).save(expected);

        Assertions.assertEquals(expected, result);
    }

    @Test
    void update(){
        User user = User.builder().email("micaela@gmail.com").build();

        Mockito.when(userRepository.save(user)).thenReturn(user);

        User obtainedUser = userService.update(user);

        Mockito.verify(userRepository).save(user);
        Assertions.assertEquals(user, obtainedUser);
    }

    @Test
    void register(){
        RegistrationRequest request = new RegistrationRequest
                ("micaela@gmail.com", "1234", "micaela", RoleName.ROLE_ADMIN);
        User expected = User.builder()
                .email("micaela@gmail.com").password("encoded").fullname("micaela").roles(Set.of(new Role(RoleName.ROLE_ADMIN))).build();

        Mockito.when(userRepository.findByEmail("micaela@gmail.com")).thenReturn(Optional.empty());
        Mockito.when(roleRepository.findByName(RoleName.ROLE_ADMIN)).thenReturn(Optional.of(new Role(RoleName.ROLE_ADMIN)));
        Mockito.when(bCryptPasswordEncoder.encode("1234")).thenReturn("encoded");
        Mockito.when(userRepository.save(expected)).then(returnsFirstArg());

        User result = userService.register(request);

        Mockito.verify(userRepository).findByEmail("micaela@gmail.com");
        Mockito.verify(roleRepository).findByName(RoleName.ROLE_ADMIN);
        Mockito.verify(bCryptPasswordEncoder).encode("1234");
        Mockito.verify(userRepository).save(expected);

        Assertions.assertEquals(expected, result);
    }

    @Test
    void register_alreadyExists(){
        RegistrationRequest request = new RegistrationRequest
                ("micaela@gmail.com", "1234", "micaela", RoleName.ROLE_ADMIN);

        Mockito.when(userRepository.findByEmail("micaela@gmail.com"))
                .thenReturn(Optional.of(User.builder().email("micaela@gmail.com").build()));

        UserAlreadyExistsException exception = Assertions.assertThrows(UserAlreadyExistsException.class,
                () -> userService.register(request));
        Assertions.assertEquals("User with email: micaela@gmail.com already exists", exception.getMessage());

        Mockito.verify(userRepository).findByEmail("micaela@gmail.com");
    }

    @Test
    void register_roleNotFound(){
        RegistrationRequest request = new RegistrationRequest
                ("micaela@gmail.com", "1234", "micaela", RoleName.ROLE_ADMIN);

        Mockito.when(userRepository.findByEmail("micaela@gmail.com")).thenReturn(Optional.empty());
        Mockito.when(roleRepository.findByName(RoleName.ROLE_ADMIN)).thenReturn(Optional.empty());

        RoleNotFoundException exception = Assertions.assertThrows(RoleNotFoundException.class,
                () -> userService.register(request));
        Assertions.assertEquals("Role ROLE_ADMIN not found", exception.getMessage());

        Mockito.verify(userRepository).findByEmail("micaela@gmail.com");
        Mockito.verify(roleRepository).findByName(RoleName.ROLE_ADMIN);
    }

    @Test
    void loadUserByUsername_userNotFound(){
        Mockito.when(userRepository.findByEmail("micaela@gmail.com")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = Assertions.assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("micaela@gmail.com"));
        Assertions.assertEquals("User with email: micaela@gmail.com not found", exception.getMessage());

        Mockito.verify(userRepository).findByEmail("micaela@gmail.com");
    }

    @Test
    void delete(){
        User user = User.builder().email("micaela@gmail.com").build();

        Mockito.when(userRepository.findByEmail("micaela@gmail.com")).thenReturn(Optional.of(user));

        String deletedEmail = userService.delete("micaela@gmail.com");

        Mockito.verify(userRepository).findByEmail("micaela@gmail.com");
        Mockito.verify(userRepository).deleteByEmail("micaela@gmail.com");
        Assertions.assertEquals("micaela@gmail.com", deletedEmail);
    }

    @Test
    void delete_userNotFound(){
        Mockito.when(userRepository.findByEmail("micaela@gmail.com")).thenReturn(Optional.empty());

        UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class, () -> userService.delete("micaela@gmail.com"));

        Mockito.verify(userRepository).findByEmail("micaela@gmail.com");
        Assertions.assertEquals("User with email: micaela@gmail.com not found", exception.getMessage());
    }
}
