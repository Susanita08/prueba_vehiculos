package com.ing.interview;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class InterviewApplicationTests {

    @Test
    void startsApplicationAndLoadsContext() {
        assertDoesNotThrow(()-> InterviewApplication.main(new String[]{}));
    }
}
