package br.com.claro.discord.bot.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.claro.discord.bot.events.EventListener;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;

@Configuration
public class DiscordBotConfig {

	@Value("${discordToken}")
	private String discordToken;

	@Bean
	public <T extends Event> GatewayDiscordClient gatewayDiscordClient(List<EventListener<T>> listeners) {
		GatewayDiscordClient client = DiscordClientBuilder.create(discordToken).build().login().block();
		for (EventListener<T> eventListener : listeners) {
			client.on(eventListener.getEventType()).flatMap(eventListener::execute)
					.onErrorResume(eventListener::handleError).subscribe();
		}
		return client;
	}

}
