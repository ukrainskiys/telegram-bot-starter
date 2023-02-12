package xyz.ukrainskiys.telegrambotstarter.handlers;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public
class HandlersStorage {
	private final HashMap<String, THandler> commandHandlers = new HashMap<>();
	private final List<THandler> handlers = new ArrayList<>();

	public boolean isCommands(String cmd) {
		return commandHandlers.containsKey(cmd);
	}

	public Optional<THandler> getHandler() {
		return handlers.isEmpty() ? Optional.empty() : Optional.of(handlers.get(0));
	}

	public Optional<THandler> getCommandHandler(String command) {
		return Optional.ofNullable(commandHandlers.get(command));
	}

	public void addHandler(THandler handler) {
		handlers.add(handler);
	}

	public void addCommandHandler(TCommandHandler commandHandler) {
		commandHandlers.put(commandHandler.command(), commandHandler);
	}

	public void addCommandsHandler(TCommandsHandler commandHandler) {
		commandHandler.commands().forEach(cmd -> commandHandlers.put(cmd, commandHandler));
	}
}
