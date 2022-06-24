package org.raki.sampleapp.application.command;

import org.raki.sampleapp.application.dto.GreetingDto;
import org.raki.sampleapp.application.Command;
import org.raki.sampleapp.domain.GreetingRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateGreeting implements Command<GreetingDto> {

    private final GreetingRepository greetingRepository;

    public CreateGreeting(GreetingRepository greetingRepository) {
        this.greetingRepository = greetingRepository;
    }

    @Override
    public void dispatch(GreetingDto command) {
        this.greetingRepository.saveGreeting(command.toDomain());
    }
}
