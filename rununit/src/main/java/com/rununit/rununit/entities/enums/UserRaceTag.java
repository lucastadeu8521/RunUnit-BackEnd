package com.rununit.rununit.entities.enums;

import lombok.Getter;

@Getter
public enum UserRaceTag {
    FAVORITE(1),
    REMINDER(2),
    WATCHLIST(3);

    private final int code;

     UserRaceTag (int code){
        this.code = code;
    }

    public static UserRaceTag valueOf(int code) {
        for (UserRaceTag value: UserRaceTag.values()) {
            if(value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid OrderUserRaceTag code");
    }
}