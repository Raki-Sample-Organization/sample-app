package org.raki.sampleapp.application.dao;

import org.raki.sampleapp.application.dto.GreetingDto;

import java.util.List;

public interface GreetingDao {
    List<GreetingDto> listByLanguage(String language);
}
