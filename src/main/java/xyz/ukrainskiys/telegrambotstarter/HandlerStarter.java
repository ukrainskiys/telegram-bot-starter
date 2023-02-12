package xyz.ukrainskiys.telegrambotstarter;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import xyz.ukrainskiys.telegrambotstarter.handlers.HandlersStorage;
import xyz.ukrainskiys.telegrambotstarter.utils.PengradUtils;

import java.util.Objects;
import java.util.Optional;

@Component
class HandlerStarter implements ApplicationListener<ContextRefreshedEvent> {
	private final TelegramBot bot;
	private final HandlersStorage handlersStorage;

	HandlerStarter(@Value("${telegram-bot.token}") String token, HandlersStorage handlersStorage) {
		this.bot = new TelegramBot(token);
		this.handlersStorage = handlersStorage;
	}

	@Override
	public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {
		bot.setUpdatesListener(updates -> {
			for (Update update : updates) {
				if (update.message() == null) {
					continue;
				}

				Long chatId = PengradUtils.getChatId(update);
				Optional<String> optional = PengradUtils.getText(update);

				optional.ifPresentOrElse(text -> {
						if (handlersStorage.isCommands(text)) {
							handlersStorage.getCommandHandler(text)
								.ifPresent(handler -> bot.execute(handler.execute(update, Objects.requireNonNull(chatId))));
						} else {
							handlersStorage.getHandler()
								.ifPresent(handler -> bot.execute(handler.execute(update, chatId)));
						}
					},
					() -> {
					});
			}

			return UpdatesListener.CONFIRMED_UPDATES_ALL;
		});
	}
}
