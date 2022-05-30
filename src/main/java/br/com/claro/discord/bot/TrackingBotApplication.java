package br.com.claro.discord.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = { "br.com.claro.discord.bot", "br.com.claro.whatsapp.tracking.service",
		"br.com.claro.whatsapp.tracking.service.aws", "br.com.claro.whatsapp.tracking.mapper",
		"br.com.claro.whatsapp.tracking.config" })
@EnableJpaRepositories("br.com.claro.whatsapp.tracking.persistence.repository")
@EntityScan("br.com.claro.whatsapp.tracking.persistence.entity")
public class TrackingBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrackingBotApplication.class, args);
	}

}
