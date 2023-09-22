package com.yagmurmuslu.traveltodo.dao;

import com.yagmurmuslu.traveltodo.model.User;

import java.util.List;

public interface UserDao {

    List<User> findAll();

    List<User> getAllUser();

    User getUserById(int userId);

    User findByUserName(String userName);

    User create (User newUser);

    void deleteUser(int userId);

    void update(User updateUser);
}
