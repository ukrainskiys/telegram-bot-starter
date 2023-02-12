package xyz.ukrainskiys.telegrambotstarter.handlers;

import org.springframework.stereotype.Component;
import xyz.ukrainskiys.telegrambotstarter.handlers.target.TargetType.Type;

import java.util.*;

@Component
public
class HandlersStorage {
	private final HashMap<String, TCommandHandler> commandHandlers = new HashMap<>();
	private final HashMap<Type, List<THandler>> handlers = new HashMap<>();

	public boolean isCommands(String cmd) {
		return commandHandlers.containsKey(cmd);
	}

	public Optional<List<THandler>> getHandlers(Type type) {
		List<THandler> tHandlers = handlers.get(type);
		if (tHandlers == null) {
			return Optional.empty();
		}
		return Optional.of(tHandlers);
	}

	public Optional<TCommandHandler> getCommandHandler(String command) {
		return Optional.ofNullable(commandHandlers.get(command));
	}

	public void addHandler(Type type, THandler handler) {
		List<THandler> tHandlers = handlers.get(type);
		if (tHandlers == null) {
			tHandlers = new ArrayList<>();
			tHandlers.add(handler);
		} else {
			tHandlers.add(handler);
		}
		handlers.put(type, tHandlers);
	}

	public void addCommandHandler(String command, TCommandHandler commandHandler) {
		commandHandlers.putIfAbsent(command.startsWith("/") ? command : "/" + command, commandHandler);
	}
}
