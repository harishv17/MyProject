package org.example.transformer.validator;

import org.example.validator.UuidValidator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;

public class UuidValidatorTest {


    @ParameterizedTest
    @NullAndEmptySource
    @CsvSource({"' '" , "Test", "/content/document"})
    void testIsUuid_WithInvalidUuid(String invalidUuid) {
        assertThat(UuidValidator.isUuid(invalidUuid)).isFalse();
    }

    @ParameterizedTest
    @CsvSource("76b72d53-43ea-4630-97a1-b8664dba8197")
    void testIsUuid_WithValidUuid(String invalidUuid) {
        assertThat(UuidValidator.isUuid(invalidUuid)).isTrue();
    }
}
