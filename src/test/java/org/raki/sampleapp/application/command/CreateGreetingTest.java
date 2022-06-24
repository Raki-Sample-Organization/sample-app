package org.raki.sampleapp.application.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.raki.sampleapp.application.dto.GreetingDto;
import org.raki.sampleapp.domain.GreetingRepository;

import java.util.UUID;

public class CreateGreetingTest {

    protected GreetingRepository greetingRepository;

    @BeforeEach
    protected void setUp() {
        greetingRepository = Mockito.mock(GreetingRepository.class);
    }

    @Test
    void dispatch() {
        GreetingDto greetingDto = new GreetingDto(UUID.randomUUID().toString(), "SPANISH", "¡Hola!");
        this.greetingRepository.saveGreeting(greetingDto.toDomain());
    }
}
