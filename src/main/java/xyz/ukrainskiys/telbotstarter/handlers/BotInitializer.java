package xyz.ukrainskiys.telbotstarter.handlers;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

@Component
class BotInitializer implements ApplicationListener<ContextRefreshedEvent> {
	private final TelegramBot bot;
	private final BotUpdateHandlerStorage handlerStorage;

	BotInitializer(
		@Autowired(required = false) TelegramBot telegramBot,
		@Autowired(required = false) String token,
//		@Value("${telegram.bot-token}") String telegramBotToken,
		BotUpdateHandlerStorage handlerStorage
	) {
		if (telegramBot == null) {
			this.bot = new TelegramBot(token);
		} else {
			this.bot = telegramBot;
		}

		this.handlerStorage = handlerStorage;
	}

	@Override
	public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {
		bot.setUpdatesListener(updates -> {
			for (Update update : updates) {
				if (update.message() == null) {
					continue;
				}
				Message msg = update.message();
				if (msg.text() == null) {
					continue;
				}

				if (handlerStorage.isCommands(msg.text())) {
					handlerStorage.getCmdHandler(msg.text())
						.ifPresent(function -> bot.execute(function.apply(update)));
				} else {
					handlerStorage.getMainHandler()
						.ifPresent(function -> bot.execute(function.apply(update)));
				}
			}

			return UpdatesListener.CONFIRMED_UPDATES_ALL;
		});
	}
}
