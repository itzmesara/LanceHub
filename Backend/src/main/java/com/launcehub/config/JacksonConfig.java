package com.launcehub.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        Hibernate6Module hibernate6Module = new Hibernate6Module();
        
        // Configure the module to handle lazy loading
        // LAZY: Will serialize lazy-loaded properties as null
        // EAGER: Will force-load lazy properties (not recommended)
        hibernate6Module.configure(Hibernate6Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS, true);
        hibernate6Module.configure(Hibernate6Module.Feature.WRITE_MISSING_ENTITIES_AS_NULL, true);
        
        mapper.registerModule(hibernate6Module);
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
