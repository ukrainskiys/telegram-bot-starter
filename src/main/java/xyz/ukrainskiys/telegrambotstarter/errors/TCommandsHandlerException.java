package xyz.ukrainskiys.telegrambotstarter.errors;

public class TCommandsHandlerException extends RuntimeException {
	public TCommandsHandlerException(Class<?> cls) {
		super(String.format("TCommandHandler [%s] must have @TargetCommands annotation", cls.getName()));
	}
}
