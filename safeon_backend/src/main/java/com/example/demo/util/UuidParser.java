package com.example.demo.util;


import java.util.UUID;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UuidParser {
    
    public static UUID parseUUID(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("UUID 형식이 아닙니다.");
        }
    }
}
