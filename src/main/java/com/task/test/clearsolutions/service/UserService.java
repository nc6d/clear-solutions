package com.task.test.clearsolutions.service;

import com.task.test.clearsolutions.exception.Status409UserAlreadyRegisteredException;
import com.task.test.clearsolutions.exception.Status431UserIsUnderAgeException;
import com.task.test.clearsolutions.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user) throws Status409UserAlreadyRegisteredException, Status431UserIsUnderAgeException;

    User getUserById(Long userId);

    void deleteUserById(Long userId);

    List<User> findAllUsers();


}
