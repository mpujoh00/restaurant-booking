package com.restaurant.booking.user.service.service;

import com.restaurant.booking.feign.client.RestaurantProxy;
import com.restaurant.booking.feign.client.exception.NotFoundException;
import com.restaurant.booking.restaurant.model.Restaurant;
import com.restaurant.booking.user.model.*;
import com.restaurant.booking.user.service.exception.*;
import com.restaurant.booking.user.service.repository.RoleRepository;
import com.restaurant.booking.user.service.repository.UserRepository;
import com.restaurant.booking.user.service.security.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final RestaurantProxy restaurantProxy;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepository, RestaurantProxy restaurantProxy, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.restaurantProxy = restaurantProxy;
        this.jwtUtils = jwtUtils;
    }

    public List<User> findAllUsers(){
        log.info("Looking for all users");
        return userRepository.findAll();
    }

    @Override
    //@Cacheable(value = "user")
    public User findByEmail(String email){
        // if the user with that email doesn't exist, throws an exception
        log.info("Looking for user with email: {}", email);
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    }

    @Override
    public User findById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User save(User user){
        log.info("Saving new user with email: {} and roles: {}", user.getEmail(), user.getRoles().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    //@CachePut(value = "user", key = "#user.email")
    public User update(User user, UpdateRequest updateRequest) {
        log.info("Updating user with email: {} and roles: {}", user.getEmail(), user.getRoles());

        if (updateRequest.getFullname() != null)
            user.setFullname(updateRequest.getFullname());
        if (updateRequest.getPassword() != null)
            user.setPassword(updateRequest.getPassword());

        return userRepository.save(user);
    }

    @Override
    public void updatePassword(User user, String newPassword) {

        log.info("Updating user's with email {} password", user.getEmail());
        user.setPassword(newPassword);
        save(user);
    }

    @Override
    public User registerBaseUser(RegistrationRequest request) {

        log.info("Registering user with email: {}", request.getEmail());

        if(request.getRole().equals(RoleName.ROLE_ADMIN)){
            log.error("Can't register a user with role admin");
            throw new InvalidUserRegistration(request.getEmail());
        }
        return registerUser(request);
    }

    @Override
    public User registerAdminUser(RegistrationRequest request) {

        log.info("Registering admin user with email: {}", request.getEmail());
        request.setRole(RoleName.ROLE_ADMIN);
        return registerUser(request);
    }

    private User registerUser(RegistrationRequest request) {

        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            log.error("User with email: {} already exists", request.getEmail());
            throw new UserAlreadyExistsException(request.getEmail());
        }
        Role role = roleRepository.findByName(request.getRole()).orElseThrow(() -> new RoleNotFoundException(request.getRole().name()));

        User user = User.builder().
                email(request.getEmail()).password(request.getPassword()).fullname(request.getFullname())
                .status(UserStatus.ENABLED).roles(Set.of(role))
                .build();

        return save(user);
    }

    @Override
    @Transactional
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        try{
            log.info("Logging in user with email: {}", email);
            return findByEmail(email);

        } catch (UserNotFoundException e){
            throw new UsernameNotFoundException("User with email: " + email + " not found");
        }
    }

    @Override
    public void deleteAdmin(User user) {
        if(user.getRolesList().get(0).getName().equals(RoleName.ROLE_ADMIN)){
            delete(user);
        }
        else{
            log.error("Can't delete user with email {}, not an admin", user.getEmail());
            throw new IncorrectAdminDeletion(user.getEmail());
        }
    }

    public void delete(User user) {
        log.info("Deleting user with email: {}", user.getEmail());
        userRepository.delete(user);
    }

    @Override
    public User changeUserStatus(User user) {
        log.info("Changing user's with email {} status", user.getEmail());
        user.setStatus(user.getStatus().getNextStatus());
        return userRepository.save(user);
    }

    @Override
    public void addRestaurant(User user, String restaurantId) {
        log.info("Adding restaurant to user with email {}", user.getEmail());

        if(!user.getRolesList().get(0).getName().equals(RoleName.ROLE_RESTAURANT)){
            log.error("Can't add restaurant, user with email {} doesn't have restaurant role", user.getEmail());
            throw new IncorrectRoleException(user.getEmail(), RoleName.ROLE_RESTAURANT.name());
        }
        else if(user.getRestaurant() != null){
            log.error("Can't add restaurant, user with email {} already has a restaurant", user.getEmail());
            throw new RestaurantAlreadyExistsException(user.getEmail());
        }
        user.setRestaurant(getRestaurant(restaurantId));
        userRepository.save(user);
    }

    private Restaurant getRestaurant(String restaurantId){
        try{
            return restaurantProxy.getRestaurant(jwtUtils.getAuthorizationHeader(), restaurantId);
        }catch (NotFoundException e){
            throw new RestaurantNotFoundException(restaurantId);
        }
    }
}
