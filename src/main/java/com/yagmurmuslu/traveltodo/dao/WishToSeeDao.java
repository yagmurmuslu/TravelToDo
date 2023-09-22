package com.yagmurmuslu.traveltodo.dao;

import com.yagmurmuslu.traveltodo.model.WishToSee;

import java.util.List;

public interface WishToSeeDao {

    List<WishToSee> listAll();

    List<WishToSee> listByUserId(int userId);

    WishToSee listByWishId(int wishId);

    WishToSee listByPlace (String placeName);

    List<WishToSee> listByCity(String cityName);

    WishToSee createNewOne(WishToSee newWishToSee);

    void update (WishToSee updateWishToSee);

    void delete (int wishId);

}
