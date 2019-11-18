package com.workshop.sudokubook

import com.workshop.sudokubook.entity.BaseEntity
import com.workshop.sudokubook.jpa.StandaloneTestJpaConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory

@Configuration
internal class TestConfig : StandaloneTestJpaConfig(entityClass = BaseEntity::class, hbm2ddl = true) {

    @Bean
    fun entityManager(emf: EntityManagerFactory): EntityManager {
        return emf.createEntityManager()
    }
}
