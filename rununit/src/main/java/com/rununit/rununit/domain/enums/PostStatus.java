package com.rununit.rununit.domain.enums;

import lombok.Getter;

@Getter
public enum PostStatus {
    DRAFT(1),
    PENDING_REVIEW(2),
    PUBLISHED(3),
    ARCHIVED(4);
    
    private final int code;

    PostStatus(int code){this.code = code;}

    public static PostStatus valueOf(int code) {
        for (PostStatus value: PostStatus.values()) {
            if(value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid OrderPostStatus code");
    }

    }