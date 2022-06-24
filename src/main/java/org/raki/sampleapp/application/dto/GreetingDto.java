package org.raki.sampleapp.application.dto;

import org.raki.sampleapp.domain.Greeting;

import java.io.Serial;
import java.io.Serializable;

public record GreetingDto(String id, String language, String content) implements Serializable {
    public Greeting toDomain() {
        return new Greeting(this.language, this.content);
    }
}
