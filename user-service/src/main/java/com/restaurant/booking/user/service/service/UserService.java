package com.restaurant.booking.user.service.service;

import com.restaurant.booking.user.model.RegistrationRequest;
import com.restaurant.booking.user.model.User;
import com.restaurant.booking.user.service.exception.UserAlreadyExistsException;

import java.util.List;


public interface UserService {

    /**
     *
     * @return
     */
    List<User> findAllUsers();

    /**
     *
     * @param email
     * @return
     */
    User findByEmail(String email);

    /**
     * @param user
     * @return
     */
    User save(User user);

    /**
     *
     * @param request
     * @return
     */
    User register(RegistrationRequest request) throws UserAlreadyExistsException;


}
