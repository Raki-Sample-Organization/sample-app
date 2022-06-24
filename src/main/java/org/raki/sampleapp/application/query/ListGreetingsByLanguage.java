package org.raki.sampleapp.application.query;

import org.raki.sampleapp.application.Query;
import org.raki.sampleapp.application.dao.GreetingDao;
import org.raki.sampleapp.application.dto.GreetingDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListGreetingsByLanguage implements Query<String, List<GreetingDto>> {

    private final GreetingDao greetingDao;

    public ListGreetingsByLanguage(GreetingDao greetingDao) {
        this.greetingDao = greetingDao;
    }

    @Override
    public List<GreetingDto> ask(String language) {
        return greetingDao.listByLanguage(language.toUpperCase());
    }
}
