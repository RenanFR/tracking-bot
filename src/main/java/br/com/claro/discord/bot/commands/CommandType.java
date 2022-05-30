package br.com.claro.discord.bot.commands;

public enum CommandType {
	TRACKINGS(CommandType.TRACKINGS_RAW_CMD),
	OLA(CommandType.OLA_RAW_CMD);
	
	public static final String TRACKINGS_RAW_CMD = "trackings";
	public static final String OLA_RAW_CMD = "ola";
	
	String rawCommand;

	CommandType(String rawCommand) {
		this.rawCommand = rawCommand;
		
	}
}
