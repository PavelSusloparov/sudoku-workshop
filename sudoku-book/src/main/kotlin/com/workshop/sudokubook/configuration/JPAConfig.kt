package com.workshop.sudokubook.configuration

import com.wework.jpa.JpaBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JPAConfig {

    @Bean
    fun jpaBean() = JpaBean()
}
