package com.tienda.web.app;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
@EntityScan("com.tienda.web.app.models.entity")
public class TiendaWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(TiendaWebApplication.class, args);
	}
	
    @Bean
    public ApplicationRunner applicationRunner(JdbcTemplate jdbcTemplate) {
        return args -> {
            jdbcTemplate.execute("SELECT 1");
        };
    }

}
