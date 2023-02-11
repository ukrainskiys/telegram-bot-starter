package xyz.ukrainskiys.telbotstarter.handlers;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Component
public class BotUpdateHandlerStorage {
	private final Map<String, Function<Update, BaseRequest<?, ?>>> cmdHandlers = new HashMap<>();
	private Function<Update, BaseRequest<?, ?>> mainHandler;

	public boolean isCommands(String cmd) {
		return cmdHandlers.containsKey(cmd);
	}
	public Optional<Function<Update, BaseRequest<?, ?>>> getMainHandler() {
		return Optional.ofNullable(mainHandler);
	}

	public Optional<Function<Update, BaseRequest<?, ?>>> getCmdHandler(String command) {
		return Optional.ofNullable(cmdHandlers.get(command));
	}

	public void setMainHandler(Function<Update, BaseRequest<?, ?>> mainHandler) {
		if (this.mainHandler != null) {
			throw new BeanCreationException("Main handler @TUpdate must be one");
		}
		this.mainHandler = mainHandler;
	}

	public void addCmdHandler(String cmd, Function<Update, BaseRequest<?, ?>> method) {
		if (cmdHandlers.containsKey(cmd)) {
			throw new BeanCreationException(String.format("Handler for command=[%s] must be one", cmd));
		}
		cmdHandlers.put(cmd, method);
	}
}
