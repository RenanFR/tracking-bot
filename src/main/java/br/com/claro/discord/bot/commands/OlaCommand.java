package br.com.claro.discord.bot.commands;

import java.util.List;

import org.springframework.stereotype.Service;

import discord4j.core.object.command.ApplicationCommandInteractionOption;

@Service(CommandType.OLA_RAW_CMD)
public class OlaCommand implements Command {

	@Override
	public String execute(List<ApplicationCommandInteractionOption> cmdOptions) {
		
		String nome = cmdOptions.stream().filter(option -> option.getName().equals("nome"))
				.map(ApplicationCommandInteractionOption::getValue).map(value -> value.get().asString()).findAny()
				.get();
		return "Olá " + nome + ", tudo bem?";
	}

}
