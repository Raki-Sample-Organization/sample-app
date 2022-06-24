package org.raki.sampleapp.domain;

import java.util.UUID;

public record Greeting(UUID id, Language language, String content) {

    public Greeting(String language, String content) {
        this(UUID.randomUUID(), Language.valueOf(language), content);
    }
}
