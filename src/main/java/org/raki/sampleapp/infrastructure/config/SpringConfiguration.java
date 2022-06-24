package org.raki.sampleapp.infrastructure.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"org.raki.sampleapp.infrastructure.persistence"})
public class SpringConfiguration {
}
