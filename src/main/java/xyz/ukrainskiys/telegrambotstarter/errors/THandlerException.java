package xyz.ukrainskiys.telegrambotstarter.errors;

public class THandlerException extends RuntimeException {
	public THandlerException(Class<?> cls) {
		super(String.format("THandler [%s] must have @TargetType annotation", cls.getName()));
	}
}
