package br.com.claro.discord.bot.config;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.claro.discord.bot.commands.Command;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;

@Configuration
public class DiscordBotConfig {

	@Value("${discordToken}")
	private String discordToken;

	@Value("${discordApplicationId}")
	private long discordApplicationId;

	@Bean
	public <T extends Event> GatewayDiscordClient gatewayDiscordClient(
			@Autowired Map<String, Command> commandHandlersMap) {
		GatewayDiscordClient client = DiscordClientBuilder.create(discordToken).build().login().block();
		client.on(ChatInputInteractionEvent.class).flatMap(commandEvent -> {
			Command commandHandler = commandHandlersMap.get(commandEvent.getCommandName());
			if (Optional.ofNullable(commandHandler).isPresent()) {
				return commandEvent.reply(commandHandler.execute(commandEvent.getOptions()));

			}
			return commandEvent.reply("Comando não existe no bot de tracking");
		}).subscribe();

		return client;
	}

	@Bean
	public GlobalCommandRegistrar globalCommandRegistrar(@Autowired GatewayDiscordClient gatewayDiscordClient)
			throws IOException {
		GlobalCommandRegistrar globalCommandRegistrar = new GlobalCommandRegistrar(
				gatewayDiscordClient.getRestClient());
		globalCommandRegistrar.registerCommands(List.of("trackings.json"));
		return globalCommandRegistrar;

	}

}
