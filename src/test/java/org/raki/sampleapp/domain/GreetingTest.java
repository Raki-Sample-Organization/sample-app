package org.raki.sampleapp.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GreetingTest {
    @Test
    void createGreeting() {
        Greeting greeting = new Greeting("ENGLISH", "Hello!");
        Assertions.assertEquals(greeting.language().name(), "ENGLISH");
        Assertions.assertEquals(greeting.content(), "Hello!");
    }
}
