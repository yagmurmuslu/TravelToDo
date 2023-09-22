package com.yagmurmuslu.traveltodo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class WishToSee {

    private int wishId;
    private String city;
    private String palaceName;
    private String address;
    private Boolean forKids;
    private Boolean completed;
    private int userId;

}
