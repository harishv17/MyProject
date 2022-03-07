package org.example.validator;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class UuidValidator {
    public static boolean isUuid (String input) {
        try {
            if (StringUtils.isBlank(input)) {
                return false;
            }
            UUID.fromString(input);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
