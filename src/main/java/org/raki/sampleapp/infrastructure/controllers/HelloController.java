package org.raki.sampleapp.infrastructure.controllers;

import org.raki.sampleapp.application.command.CreateGreeting;
import org.raki.sampleapp.application.dto.GreetingDto;
import org.raki.sampleapp.application.query.ListGreetingsByLanguage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("greetings")
public class HelloController {

    @Autowired
    private ListGreetingsByLanguage listGreetingsByLanguage;

    @Autowired
    private CreateGreeting createGreeting;

    @GetMapping
    public ResponseEntity<List<GreetingDto>> greetingsByLanguage(@RequestParam String language) {
        return new ResponseEntity<>(listGreetingsByLanguage.ask(language), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> createGreeting(@RequestBody GreetingDto greetingDto) {
        createGreeting.dispatch(greetingDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
