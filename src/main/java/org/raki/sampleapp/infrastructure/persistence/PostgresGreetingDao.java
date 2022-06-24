package org.raki.sampleapp.infrastructure.persistence;

import org.jooq.DSLContext;
import org.raki.sampleapp.application.dao.GreetingDao;
import org.raki.sampleapp.application.dto.GreetingDto;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.raki.generated.jooq.Tables.GREETINGS;

@Repository
public class PostgresGreetingDao implements GreetingDao {

    private final DSLContext dsl;

    public PostgresGreetingDao(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public List<GreetingDto> listByLanguage(String language) {
        return this.dsl.select()
                .from(GREETINGS)
                .where(GREETINGS.LANGUAGE.eq(language))
                .fetchInto(GreetingDto.class);
    }
}
