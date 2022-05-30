# Tracking discord bot
Um Bot no Discord para que QA e CSM possam recuperar via comando slash a qualquer momento os trackings de qualquer período
É uma aplicação reativa criada com o discord4j que provê o backend que responde pelo comando /trackings no Discord
Por ser um backend reativo precisamos da dependência do webflux no pom
```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </dependency>
```
```xml
		<dependency>
			<groupId>com.discord4j</groupId>
			<artifactId>discord4j-core</artifactId>
			<version>3.2.2</version>
		</dependency>
```
## Criando e configurando o Bot
- Após criada a aplicação e o Bot no portal developer do Discord copiamos e incluímos em nossas variáveis de ambiente da aplicação o Token e o ApplicationId
  - Com esses dois em uma @Configuration iremos configurar o Gateway de comunicação com o Discord
```java
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
		return client;
	}
}
```
- O Gateway configurado anteriormente será listener de eventos do tipo ChatInputInteractionEvent que são os comandos slash
```java
		client.on(ChatInputInteractionEvent.class).flatMap(commandEvent -> {
			Command commandHandler = commandHandlersMap.get(commandEvent.getCommandName());
			if (Optional.ofNullable(commandHandler).isPresent()) {
				return commandEvent.reply(commandHandler.execute(commandEvent.getOptions()));

			}
			return commandEvent.reply("Comando não existe no bot de tracking");
		}).subscribe();
```
- Por meio do nome do comando identificamos o handler correspondente seguindo o padrão Command
- Temos uma interface Command e as respectivas implementações para atender a cada tipo de comando slash customizado
- O spring automaticamente preenche um Map cujas chaves são os Ids das @Service de implementação
- No momento em que o comando é chamado o GatewayDiscordClient identifica e feito um get no mapa do spring encontra o handler responsável pelo comando e chama o execute que irá aplicar a lógica do comando e enviar a resposta ao canal de volta ao usuário após montar a String por meio do reply
```java
public interface Command {

	String execute(List<ApplicationCommandInteractionOption> cmdOptions);

}
```
```java
@Service(CommandType.TRACKINGS_RAW_CMD)
public class FetchTrackingCommand implements Command {
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	@Override
	public String execute(List<ApplicationCommandInteractionOption> cmdOptions) {
		String commandArguments = cmdOptions.stream().map(ApplicationCommandInteractionOption::getValue).map(value -> value.get().asString())
				.collect(Collectors.joining(" - "));
		LOGGER.info("COMANDO trackings INVOCADO COM OS ARGUMENTOS {}", commandArguments);
		return "Things to do today:\n - write a bot\n - eat lunch\n - play a game";

	}

}
```
## Registrando nosso comando slash
Para registrar os comandos slash globais junto ao servidor no Discord temos o payload contendo a assinatura do comando dentro do diretório commands no formato json
- Temos a classe GlobalCommandRegistrar que é registrada como @Bean e faz o registro dos comandos dentro do diretório por meio da chamada rest definida pela API do Discord
```java
	@Bean
	public GlobalCommandRegistrar globalCommandRegistrar(@Autowired GatewayDiscordClient gatewayDiscordClient)
			throws IOException {
		GlobalCommandRegistrar globalCommandRegistrar = new GlobalCommandRegistrar(
				gatewayDiscordClient.getRestClient());
		globalCommandRegistrar.registerCommands(List.of("trackings.json"));
		return globalCommandRegistrar;

	}
```
- Esse payload contem o nome pelo qual o comando é invocado bem como seus parâmetros e tipos
```json
{
    "name": "trackings",
    "description": "Recupera os trackings para o período selecionado",
    "options": [
        {
            "name": "inicio",
            "description": "Periodo de inicio",
            "type": 3,
            "required": true
        },
        {
            "name": "fim",
            "description": "Periodo de fim",
            "type": 3,
            "required": true
        }
    ]
}
```