package com.websystique.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories("com.websystique.persistence.repo")
@EntityScan("com.websystique.persistence.entity")
@EnableTransactionManagement
public class PersistenceConfig {
}
