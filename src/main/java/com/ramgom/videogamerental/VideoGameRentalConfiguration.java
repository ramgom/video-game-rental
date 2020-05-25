package com.ramgom.videogamerental;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VideoGameRentalConfiguration {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customJson(){
        return builder -> builder.serializationInclusion(JsonInclude.Include.NON_NULL);
    }
}
