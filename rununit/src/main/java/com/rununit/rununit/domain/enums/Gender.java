package com.rununit.rununit.domain.enums;

public enum Gender {

    M(1),
    F(2),
    O(3),;

    int code;

    Gender( int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static Gender valueOf(int code) {
        for (Gender value: Gender.values()) {
            if(value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid OrderStatus code");
    }
}
