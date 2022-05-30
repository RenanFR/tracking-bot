package br.com.claro.discord.bot.commands;

public enum CommandType {
	TRACKINGS(CommandType.TRACKINGS_RAW_CMD);
	
	public static final String TRACKINGS_RAW_CMD = "trackings";
	
	String rawCommand;

	CommandType(String rawCommand) {
		this.rawCommand = rawCommand;
		
	}
}
