package xyz.ukrainskiys.telegrambotstarter.handlers;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
class THandlerBeanPostProcessor implements BeanPostProcessor {
	private final HandlersStorage handlersStorage;

	@Lazy
	public THandlerBeanPostProcessor(HandlersStorage handlerStorage) {
		this.handlersStorage = handlerStorage;
	}

	@Override
	public Object postProcessAfterInitialization(@NotNull Object bean, @NotNull String beanName) throws BeansException {
		switch (bean) {
			case TCommandHandler tch -> handlersStorage.addCommandHandler(tch);
			case THandler th -> handlersStorage.addHandler(th);
			default -> {
			}
		}

		return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
	}
}
