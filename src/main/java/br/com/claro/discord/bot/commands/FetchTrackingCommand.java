package br.com.claro.discord.bot.commands;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.claro.whatsapp.tracking.service.TrackingService;
import discord4j.core.object.command.ApplicationCommandInteractionOption;

@Service(CommandType.TRACKINGS_RAW_CMD)
public class FetchTrackingCommand implements Command {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	private static final String DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm";
	private static final String DATE_TIME_PATTERN_CSV = "dd-MM-yyyy HH.mm";

	@Autowired
	private TrackingService trackingService;

	@Override
	public String execute(List<ApplicationCommandInteractionOption> cmdOptions) {
		String commandArguments = cmdOptions.stream().map(ApplicationCommandInteractionOption::getValue)
				.map(value -> value.get().asString()).collect(Collectors.joining(" - "));
		LOGGER.info("COMANDO trackings INVOCADO COM OS ARGUMENTOS {}", commandArguments);

		String inicio = cmdOptions.stream().filter(option -> option.getName().equals("inicio"))
				.map(ApplicationCommandInteractionOption::getValue).map(value -> value.get().asString()).findAny()
				.get();
		String fim = cmdOptions.stream().filter(option -> option.getName().equals("fim"))
				.map(ApplicationCommandInteractionOption::getValue).map(value -> value.get().asString()).findAny()
				.get();
		LocalDateTime initialPeriod = LocalDateTime.parse(inicio, DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
		LocalDateTime finalPeriod = LocalDateTime.parse(fim, DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));

		String trackingCsvKey = "trackings_adhoc_"
				+ initialPeriod.format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN_CSV)) + "_"
				+ finalPeriod.format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN_CSV));

		try {
			String presignedForUrlTrackingCsv = trackingService.fetchTrackingsAndUploadFileToS3(initialPeriod,
					finalPeriod, trackingCsvKey);
			return "O relatório de trackings está disponível em: " + presignedForUrlTrackingCsv;
		} catch (IOException e) {
			return "Erro ao gerar o relatório de trackings, contate os Devs";
		}

	}

}
