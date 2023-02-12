package xyz.ukrainskiys.telegrambotstarter.handlers;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import xyz.ukrainskiys.telegrambotstarter.errors.TCommandsHandlerException;
import xyz.ukrainskiys.telegrambotstarter.errors.THandlerException;
import xyz.ukrainskiys.telegrambotstarter.handlers.target.TargetCommands;
import xyz.ukrainskiys.telegrambotstarter.handlers.target.TargetType;

@Component
class THandlerBeanPostProcessor implements BeanPostProcessor {
	private final HandlersStorage handlersStorage;

	@Lazy
	public THandlerBeanPostProcessor(HandlersStorage handlerStorage) {
		this.handlersStorage = handlerStorage;
	}

	@Override
	public Object postProcessAfterInitialization(@NotNull Object bean, @NotNull String beanName) throws BeansException {
		Class<?> aClass = bean.getClass();

		if (bean instanceof TCommandHandler tch) {
			if (!aClass.isAnnotationPresent(TargetCommands.class)) {
				throw new TCommandsHandlerException(aClass);
			}
			String[] value = aClass.getAnnotation(TargetCommands.class).value();
			for (String cmd : value) {
				handlersStorage.addCommandHandler(cmd, tch);
			}
		} else if (bean instanceof THandler th) {
			if (!aClass.isAnnotationPresent(TargetType.class)) {
				throw new THandlerException(aClass);
			}
			TargetType.Type[] value = aClass.getAnnotation(TargetType.class).value();
			for (TargetType.Type type : value) {
				handlersStorage.addHandler(type, th);
			}
		}

		return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
	}
}
