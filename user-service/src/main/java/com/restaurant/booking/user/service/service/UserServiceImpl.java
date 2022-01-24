package com.restaurant.booking.user.service.service;

import com.restaurant.booking.user.model.RegistrationRequest;
import com.restaurant.booking.user.model.User;
import com.restaurant.booking.user.service.exception.UserAlreadyExistsException;
import com.restaurant.booking.user.service.exception.UserNotFoundException;
import com.restaurant.booking.user.service.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findByEmail(String email){
        // if the user with that email doesn't exist, throws an exception
        log.info("Looking for user with email: " + email);
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    }

    @Override
    public User save(User user){
        log.info("Saving user with email: " + user.getEmail() + " and role: " + user.getRole().name());
        // saves user to database with encoded password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User register(RegistrationRequest request) throws UserAlreadyExistsException {
        log.info("Registering user with email: " + request.getEmail());

        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            log.info("User with email: " + request.getEmail() + " already exists");
            throw new UserAlreadyExistsException(request.getEmail());
        }
        User user = User.builder().
                email(request.getEmail()).password(request.getPassword()).fullname(request.getFullname()).role(request.getRole())
                .build();

        return save(user);
    }

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        try{
            log.info("Logging in user with email: " + email);
            return findByEmail(email);

        } catch (UserNotFoundException e){
            throw new UsernameNotFoundException("User with email: " + email + " not found");
        }
    }

}
