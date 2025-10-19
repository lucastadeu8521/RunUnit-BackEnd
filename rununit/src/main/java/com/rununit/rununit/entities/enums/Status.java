package com.rununit.rununit.entities.enums;

public enum Status {

    PENDING(1),
    ACTIVE(2),
    COMPLETED(3),
    CANCELED(4),;

    int code;

     Status (int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static Status valueOf(int code) {
        for (Status value: Status.values()) {
            if(value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid OrderStatus code");
    }
}
