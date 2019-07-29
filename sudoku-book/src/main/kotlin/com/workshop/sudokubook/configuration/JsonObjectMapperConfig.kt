package com.workshop.sudokubook.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JsonObjectMapperConfig {

    @Bean("upperCamelCaseMapper")
    fun upperCamelCaseMapper(): ObjectMapper {
        val mapper = ObjectMapper()
        mapper.propertyNamingStrategy = PropertyNamingStrategy.UPPER_CAMEL_CASE
        return mapper
    }
}
