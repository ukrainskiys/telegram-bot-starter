package xyz.ukrainskiys.telbotstarter.handlers;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Function;

@Component
public class TUpdateHandlerBeanPostProcessor implements BeanPostProcessor {
	private final BotUpdateHandlerStorage handlerStorage;

	public TUpdateHandlerBeanPostProcessor(BotUpdateHandlerStorage handlerStorage) {
		this.handlerStorage = handlerStorage;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, @NotNull String beanName) throws BeansException {
		Class<?> cls = bean.getClass();
		if (cls.isAnnotationPresent(TUpdateHandler.class)) {
			Method[] methods = cls.getDeclaredMethods();

			Arrays.stream(methods)
				.filter(method -> method.isAnnotationPresent(TCommand.class))
				.forEach(method -> Arrays.stream(method.getAnnotation(TCommand.class).value())
					.map(cmd -> cmd.startsWith("/") ? cmd : "/" + cmd)
					.forEach(cmd -> handlerStorage.addCmdHandler(cmd, createFunction(bean, method))));

			Arrays.stream(methods)
				.filter(method -> method.isAnnotationPresent(TUpdate.class))
				.forEach(method -> handlerStorage.setMainHandler(createFunction(bean, method)));
		}

		return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
	}

	private Function<Update, BaseRequest<?, ?>> createFunction(Object bean, Method method) {
		return update -> {
			try {
				return (BaseRequest<?, ?>) method.invoke(bean, update);
			} catch (IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		};
	}
}
