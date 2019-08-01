package com.workshop.sudokubook.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JsonObjectMapperConfig {

    @Bean("objectMapper")
    fun upperCamelCaseMapper() = ObjectMapper()
}
