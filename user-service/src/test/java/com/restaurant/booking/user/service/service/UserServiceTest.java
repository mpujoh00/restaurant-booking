package com.restaurant.booking.user.service.service;

import com.restaurant.booking.feign.client.RestaurantProxy;
import com.restaurant.booking.feign.client.exception.NotFoundException;
import com.restaurant.booking.restaurant.model.Restaurant;
import com.restaurant.booking.user.model.*;
import com.restaurant.booking.user.service.exception.*;
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
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private RestaurantProxy restaurantProxy;

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
        UpdateRequest request = new UpdateRequest("email", "password", "name");

        Mockito.when(userRepository.save(user)).thenReturn(user);

        User obtainedUser = userService.update(user, request);

        Mockito.verify(userRepository).save(user);
        Assertions.assertEquals(user, obtainedUser);
    }

    @Test
    void updatePassword(){
        User user = User.builder().email("micaela@gmail.com").roles(Set.of(new Role(RoleName.ROLE_CLIENT))).build();

        userService.updatePassword(user, "new");

        Mockito.verify(userRepository).save(user);
    }

    @Test
    void registerBaseUser(){
        RegistrationRequest request = new RegistrationRequest
                ("micaela@gmail.com", "1234", "micaela", RoleName.ROLE_CLIENT);
        User user = User.builder()
                .email("micaela@gmail.com").password("encoded").fullname("micaela").roles(Set.of(new Role(RoleName.ROLE_CLIENT))).build();
        User expected = user;
        expected.setStatus(UserStatus.ENABLED);

        Mockito.when(userRepository.findByEmail("micaela@gmail.com")).thenReturn(Optional.empty());
        Mockito.when(roleRepository.findByName(RoleName.ROLE_CLIENT)).thenReturn(Optional.of(new Role(RoleName.ROLE_CLIENT)));
        Mockito.when(bCryptPasswordEncoder.encode("1234")).thenReturn("encoded");
        Mockito.when(userRepository.save(user)).thenReturn(expected);

        User result = userService.registerBaseUser(request);

        Mockito.verify(userRepository).findByEmail("micaela@gmail.com");
        Mockito.verify(roleRepository).findByName(RoleName.ROLE_CLIENT);
        Mockito.verify(bCryptPasswordEncoder).encode("1234");
        Mockito.verify(userRepository).save(user);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void registerBaseUser_invalid(){
        RegistrationRequest request = new RegistrationRequest
                ("micaela@gmail.com", "1234", "micaela", RoleName.ROLE_ADMIN);

        InvalidUserRegistration exception = Assertions.assertThrows(
                InvalidUserRegistration.class, () -> userService.registerBaseUser(request));
        Assertions.assertEquals("Can't register a user micaela@gmail.com with role admin", exception.getMessage());
    }

    @Test
    void registerAdminUser(){
        RegistrationRequest request = new RegistrationRequest
                ("micaela@gmail.com", "1234", "micaela", RoleName.ROLE_ADMIN);
        User user = User.builder()
                .email("micaela@gmail.com").password("encoded").fullname("micaela").roles(Set.of(new Role(RoleName.ROLE_ADMIN))).build();
        User expected = user;
        expected.setStatus(UserStatus.ENABLED);

        Mockito.when(userRepository.findByEmail("micaela@gmail.com")).thenReturn(Optional.empty());
        Mockito.when(roleRepository.findByName(RoleName.ROLE_ADMIN)).thenReturn(Optional.of(new Role(RoleName.ROLE_ADMIN)));
        Mockito.when(bCryptPasswordEncoder.encode("1234")).thenReturn("encoded");
        Mockito.when(userRepository.save(user)).thenReturn(expected);

        User result = userService.registerAdminUser(request);

        Mockito.verify(userRepository).findByEmail("micaela@gmail.com");
        Mockito.verify(roleRepository).findByName(RoleName.ROLE_ADMIN);
        Mockito.verify(bCryptPasswordEncoder).encode("1234");
        Mockito.verify(userRepository).save(user);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void register_alreadyExists(){
        RegistrationRequest request = new RegistrationRequest
                ("micaela@gmail.com", "1234", "micaela", RoleName.ROLE_ADMIN);

        Mockito.when(userRepository.findByEmail("micaela@gmail.com"))
                .thenReturn(Optional.of(User.builder().email("micaela@gmail.com").build()));

        UserAlreadyExistsException exception = Assertions.assertThrows(UserAlreadyExistsException.class,
                () -> userService.registerAdminUser(request));
        Assertions.assertEquals("User with email: micaela@gmail.com already exists", exception.getMessage());

        Mockito.verify(userRepository).findByEmail("micaela@gmail.com");
    }

    @Test
    void register_roleNotFound(){
        RegistrationRequest request = new RegistrationRequest
                ("micaela@gmail.com", "1234", "micaela", RoleName.ROLE_CLIENT);

        Mockito.when(userRepository.findByEmail("micaela@gmail.com")).thenReturn(Optional.empty());
        Mockito.when(roleRepository.findByName(RoleName.ROLE_CLIENT)).thenReturn(Optional.empty());

        RoleNotFoundException exception = Assertions.assertThrows(RoleNotFoundException.class,
                () -> userService.registerBaseUser(request));
        Assertions.assertEquals("Role ROLE_CLIENT not found", exception.getMessage());

        Mockito.verify(userRepository).findByEmail("micaela@gmail.com");
        Mockito.verify(roleRepository).findByName(RoleName.ROLE_CLIENT);
    }

    @Test
    void loadUserByUsername_userNotFound(){
        Mockito.when(userRepository.findByEmail("micaela@gmail.com")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = Assertions.assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("micaela@gmail.com"));
        Assertions.assertEquals("User with email: micaela@gmail.com not found", exception.getMessage());

        Mockito.verify(userRepository).findByEmail("micaela@gmail.com");
    }

    @Test
    void deleteAdmin(){
        User user = User.builder().roles(Set.of(new Role(RoleName.ROLE_ADMIN))).build();

        userService.deleteAdmin(user);

        Mockito.verify(userRepository).delete(user);
    }

    @Test
    void deleteAdmin_notAdmin(){
        User user = User.builder().email("micaela@gmail.com").roles(Set.of(new Role(RoleName.ROLE_CLIENT))).build();

        IncorrectAdminDeletion exception = Assertions.assertThrows(
                IncorrectAdminDeletion.class, () -> userService.deleteAdmin(user));
        Assertions.assertEquals("Can't delete user with email micaela@gmail.com, must have admin role", exception.getMessage());
    }

    @Test
    void delete(){
        User user = User.builder().build();

        userService.delete(user);

        Mockito.verify(userRepository).delete(user);
    }

    @Test
    void changeUserStatus(){
        User userBefore = User.builder().email("micaela@gmail.com").status(UserStatus.ENABLED).build();
        User userAfter = User.builder().email("micaela@gmail.com").status(UserStatus.DISABLED).build();

        Mockito.when(userRepository.save(userAfter)).thenReturn(userAfter);

        User obtainedUser = userService.changeUserStatus(userBefore);

        Mockito.verify(userRepository).save(userAfter);
        Assertions.assertEquals(userAfter, obtainedUser);
    }

    @Test
    void addRestaurant(){
        User user = User.builder().email("micaela@gmail.com").roles(Set.of(new Role(RoleName.ROLE_RESTAURANT))).build();
        Restaurant restaurant = Restaurant.builder().build();
        User userAfter = User.builder()
                .email("micaela@gmail.com").roles(Set.of(new Role(RoleName.ROLE_RESTAURANT)))
                .restaurant(restaurant).build();

        Mockito.when(restaurantProxy.getRestaurant("restaurantId")).thenReturn(restaurant);
        Mockito.when(userRepository.save(userAfter)).thenReturn(userAfter);

        userService.addRestaurant(user, "restaurantId");

        Mockito.verify(restaurantProxy).getRestaurant("restaurantId");
        Mockito.verify(userRepository).save(userAfter);
    }

    @Test
    void addRestaurant_incorrectRole(){
        User user = User.builder().email("micaela@gmail.com").roles(Set.of(new Role(RoleName.ROLE_CLIENT))).build();

        IncorrectRoleException exception = Assertions.assertThrows(
                IncorrectRoleException.class, () -> userService.addRestaurant(user, "restaurantId"));
        Assertions.assertEquals("User with email micaela@gmail.com doesn't have ROLE_RESTAURANT role", exception.getMessage());
    }

    @Test
    void addRestaurant_restaurantAlreadyExists(){
        User user = User.builder().email("micaela@gmail.com").roles(Set.of(new Role(RoleName.ROLE_RESTAURANT)))
                .restaurant(Restaurant.builder().build()).build();

        RestaurantAlreadyExistsException exception = Assertions.assertThrows(
                RestaurantAlreadyExistsException.class, () -> userService.addRestaurant(user, "restaurantId"));
        Assertions.assertEquals("Can't add restaurant, user with email micaela@gmail.com already has a restaurant", exception.getMessage());
    }

    @Test
    void addRestaurant_restaurantNotFound(){
        User user = User.builder().email("micaela@gmail.com").roles(Set.of(new Role(RoleName.ROLE_RESTAURANT))).build();

        Mockito.when(restaurantProxy.getRestaurant("restaurantId")).thenThrow(new NotFoundException());

        RestaurantNotFoundException exception = Assertions.assertThrows(
                RestaurantNotFoundException.class, () -> userService.addRestaurant(user, "restaurantId"));
        Assertions.assertEquals("Restaurant with id restaurantId not found", exception.getMessage());

        Mockito.verify(restaurantProxy).getRestaurant("restaurantId");
    }
}
