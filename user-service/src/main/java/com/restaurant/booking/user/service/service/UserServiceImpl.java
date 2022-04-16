package com.restaurant.booking.user.service.service;

import com.restaurant.booking.user.model.RegistrationRequest;
import com.restaurant.booking.user.model.Role;
import com.restaurant.booking.user.model.User;
import com.restaurant.booking.user.service.exception.RoleNotFoundException;
import com.restaurant.booking.user.service.exception.UserAlreadyExistsException;
import com.restaurant.booking.user.service.exception.UserNotFoundException;
import com.restaurant.booking.user.service.repository.RoleRepository;
import com.restaurant.booking.user.service.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Log4j2
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public List<User> findAllUsers(){
        log.info("Looking for all users");
        return userRepository.findAll();
    }

    @Override
    public User findByEmail(String email){
        // if the user with that email doesn't exist, throws an exception
        log.info("Looking for user with email: {}", email);
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    }

    @Override
    public User save(User user){
        log.info("Saving new user with email: {} and roles: {}", user.getEmail(), user.getRoles().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        log.info("Updating user with email: {} and roles: {}", user.getEmail(), user.getRoles());
        return userRepository.save(user);
    }

    @Override
    public User register(RegistrationRequest request) throws UserAlreadyExistsException {
        log.info("Registering user with email: {}", request.getEmail());

        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            log.error("User with email: {} already exists", request.getEmail());
            throw new UserAlreadyExistsException(request.getEmail());
        }
        Role role = roleRepository.findByName(request.getRole()).orElseThrow(() -> new RoleNotFoundException(request.getRole().name()));

        User user = User.builder().
                email(request.getEmail()).password(request.getPassword()).fullname(request.getFullname()).roles(Set.of(role))
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
    public String delete(String email) {
        log.info("Deleting user with email: {}", email);

        if(userRepository.findByEmail(email).isEmpty()){
            log.error("User with email: {} doesn't exist", email);
            throw new UserNotFoundException(email);
        }
        userRepository.deleteByEmail(email);
        return email;
    }
}
