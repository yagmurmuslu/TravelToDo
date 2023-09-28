package com.yagmurmuslu.traveltodo.dao;

import com.yagmurmuslu.traveltodo.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JdbcUserDao implements UserDao{

    private  final JdbcTemplate jdbcTemplate;
    public JdbcUserDao(DataSource dataSource) {this.jdbcTemplate= new JdbcTemplate(dataSource);}

    @Override
    public User getUserById(int userId) {
        try{
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM users WHERE user_id=?",
                    this::mapRowToUser,
                    userId
            );
        } catch (EmptyResultDataAccessException exception){
            return null;
        }
    }
    public List<User> getAllUser(){
        return jdbcTemplate.query(
                "SELECT * FROM users",
                this::mapRowToUser
        );
    }

    @Override
    public User findByUserName(String userName) {
        try{
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM users WHERE name = ?",
                    this::mapRowToUser,
                    userName
            );
        } catch (EmptyResultDataAccessException exception){
            return null;
        }
    }

    @Override
    public User create(User newUser) {
        Integer newId = jdbcTemplate.queryForObject(
                "INSERT INTO users (name, password_hash)" +
                        "VALUES (?, ?) RETURNING user_id;",
                Integer.class,
                newUser.getName(),
                newUser.getPassword()
        );
        return getUserById(newId);
    }

    @Override
    public void deleteUser(int userId) {
        jdbcTemplate.update("DELETE FROM users WHERE user_id = ?", userId);
    }

    @Override
    public void updateUserName(User updateUserName) {
        jdbcTemplate.update("UPDATE users SET name = ? WHERE user_id = ?", updateUserName.getName(), updateUserName.getId());
    }

    @Override
    public void updateUserPassword(User updateUserPassword) {
        jdbcTemplate.update("UPDATE users SET password_hash = ? WHERE user_id = ?", updateUserPassword.getPassword(), updateUserPassword.getId());
    }

    private User mapRowToUser (ResultSet row, int rowNum) throws SQLException {
        return new User(
                row.getInt("user_id"),
                row.getString("name"),
                row.getString("password_hash")
        );
    }

}
