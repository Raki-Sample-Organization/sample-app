package org.raki.sampleapp.infrastructure.persistence;

import org.jooq.DSLContext;
import org.raki.sampleapp.domain.Greeting;
import org.raki.sampleapp.domain.GreetingRepository;
import org.springframework.stereotype.Repository;

import static org.raki.generated.jooq.Tables.GREETINGS;

@Repository
public class PostgresGreetingRepository implements GreetingRepository {

    private final DSLContext dsl;

    public PostgresGreetingRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public void saveGreeting(Greeting greeting) {
        this.dsl.insertInto(GREETINGS, GREETINGS.ID, GREETINGS.LANGUAGE, GREETINGS.CONTENT)
                .values(greeting.id().toString(), greeting.language().name(), greeting.content())
                .execute();
    }
}
