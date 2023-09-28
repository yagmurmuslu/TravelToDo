package com.yagmurmuslu.traveltodo.dao;

import com.yagmurmuslu.traveltodo.model.User;

import java.util.List;

public interface UserDao {

    List<User> getAllUser();

    User getUserById(int userId);

    User findByUserName(String userName);

    User create (User newUser);

    void deleteUser(int userId);

    void updateUserName(User updateUserName);

    void updateUserPassword(User updateUserPassword);
}
