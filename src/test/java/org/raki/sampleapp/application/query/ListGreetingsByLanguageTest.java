package org.raki.sampleapp.application.query;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.raki.sampleapp.application.dao.GreetingDao;
import org.raki.sampleapp.application.dto.GreetingDto;
import java.util.List;
import java.util.UUID;

public class ListGreetingsByLanguageTest {
    protected GreetingDao greetingDao;

    @BeforeEach
    protected void setUp() {
        greetingDao = Mockito.mock(GreetingDao.class);
        Mockito.when(greetingDao.listByLanguage("SPANISH"))
                .thenReturn(List.of(new GreetingDto(UUID.randomUUID().toString(), "SPANISH", "¡Hola!")));
    }

    @Test
    void ask() {
        List<GreetingDto> result = this.greetingDao.listByLanguage("SPANISH");
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("¡Hola!", result.get(0).content());
    }
}
