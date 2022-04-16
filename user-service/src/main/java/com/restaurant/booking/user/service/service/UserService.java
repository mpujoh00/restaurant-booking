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
     * Saves new user or updates existing one
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

    /**
     *
     * @param user
     * @return
     */
    User update(User user);

    /**
     *
     * @param email
     */
    String delete(String email);
}
