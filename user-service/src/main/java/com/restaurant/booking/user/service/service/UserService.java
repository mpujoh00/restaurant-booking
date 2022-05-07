package com.restaurant.booking.user.service.service;

import com.restaurant.booking.user.model.RegistrationRequest;
import com.restaurant.booking.user.model.UpdateRequest;
import com.restaurant.booking.user.model.User;

import java.util.List;


public interface UserService {

    // TODO mover la lógica del controlador al servicio

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
    User registerBaseUser(RegistrationRequest request);

    /**
     *
     * @param user
     * @return
     */
    User update(User user, UpdateRequest updateRequest);

    /**
     *
     * @param user
     * @param newPassword
     */
    void updatePassword(User user, String newPassword);

    /**
     *
     * @param request
     * @return
     */
    User registerAdminUser(RegistrationRequest request);

    /**
     *
     * @param user
     */
    void delete(User user);

    /**
     *
     * @param user
     */
    void deleteAdmin(User user);

    /**
     *
     * @param user
     * @return
     */
    User changeUserStatus(User user);
}
