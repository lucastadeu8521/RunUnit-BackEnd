package com.rununit.rununit.entities.enums;

public enum UserRole {

    ADMIN(1),
    EDITOR(2),
    USER(3),;

    int code;

    UserRole(int code) {
        this.code = code;
    }

    public int getCode(){
        return code;
    }

    public static UserRole valueOf(int code) {
        for (UserRole value: UserRole.values()) {
            if(value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid OrderStatus code");
    }
}
