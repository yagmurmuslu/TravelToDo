package com.yagmurmuslu.traveltodo.dao;

import com.yagmurmuslu.traveltodo.model.WishToSee;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JdbcWishToSeeDao implements WishToSeeDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcWishToSeeDao(DataSource dataSource) {this.jdbcTemplate = new JdbcTemplate(dataSource);}

    @Override
    public List<WishToSee> listAll() {
        return jdbcTemplate.query(
                "SELECT * FROM wish_to_see ",
                this::mapRowToWishToSee
        );
    }

    @Override
    public List<WishToSee> listByUserId(int userId) {
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM wish_to_see WHERE user_id = ? ",
                    this::mapRowToWishToSee,
                    userId
            );
        }catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    @Override
    public WishToSee listByWishId(int wishId){
        try{
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM wish_to_see WHERE wish_id = ?",
                    this::mapRowToWishToSee,
                    wishId
            );
        } catch (EmptyResultDataAccessException exception) {
            return null;
        }
    }

    @Override
    public WishToSee listByPlace(String placeName) {
        try{
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM wish_to_see WHERE place_name = ?",
                    this::mapRowToWishToSee,
                    placeName
            );
        } catch ( EmptyResultDataAccessException exception) {
            return null;
        }
    }

    @Override
    public List<WishToSee> listByCity(String cityName) {
        try{
            return jdbcTemplate.query(
                    "SELECT * FROM wish_to_see WHERE LOWER(city) = ? ",
                    this::mapRowToWishToSee,
                    cityName
            );
        }catch (EmptyResultDataAccessException exception){
            return null;
        }
    }

    @Override
    public WishToSee createNewOne(WishToSee newWishToSee) {
        Integer newPlace = jdbcTemplate.queryForObject(
                "INSERT INTO wish_to_see (city, place_name, address, for_kids, completed, user_id)" +
                        "VALUES (?, ?, ?, ?, ?, ? ) RETURNING wish_id;",
                Integer.class,
                newWishToSee.getCity(),
                newWishToSee.getPlaceName(),
                newWishToSee.getAddress(),
                newWishToSee.getForKids(),
                newWishToSee.getCompleted(),
                newWishToSee.getUserId()
        );
        return listByWishId(newPlace);
    }

    @Override
    public void update(WishToSee updateWishToSee) {
        jdbcTemplate.update("UPDATE wish_to_see SET city = ? WHERE wish_id = ?", updateWishToSee.getCity(), updateWishToSee.getWishId());
    }

    @Override
    public void delete(int wishId) {
        jdbcTemplate.update("DELETE FROM wish_to_see WHERE wish_id = ?", wishId);

    }

    private WishToSee mapRowToWishToSee (ResultSet row, int rowNum) throws SQLException {
        return new WishToSee(
                row.getInt("wish_id"),
                row.getString("city"),
                row.getString("place_name"),
                row.getString("address"),
                row.getBoolean("for_kids"),
                row.getBoolean("completed"),
                row.getInt("user_id")
        );
    }
}
