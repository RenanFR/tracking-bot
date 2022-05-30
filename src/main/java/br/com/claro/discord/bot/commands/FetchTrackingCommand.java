package br.com.claro.discord.bot.commands;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import discord4j.core.object.command.ApplicationCommandInteractionOption;

@Service(CommandType.TRACKINGS_RAW_CMD)
public class FetchTrackingCommand implements Command {
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Override
	public String execute(List<ApplicationCommandInteractionOption> cmdOptions) {
		String commandArguments = cmdOptions.stream().map(ApplicationCommandInteractionOption::getValue).map(value -> value.get().asString())
				.collect(Collectors.joining(" - "));
		LOGGER.info("COMANDO TRACKINGS INVOCADO COM OS ARGUMENTOS {}", commandArguments);
		return "Things to do today:\n - write a bot\n - eat lunch\n - play a game";

	}

}
