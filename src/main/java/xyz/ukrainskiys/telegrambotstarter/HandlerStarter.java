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
import xyz.ukrainskiys.telegrambotstarter.handlers.TCommandHandler;
import xyz.ukrainskiys.telegrambotstarter.handlers.target.TargetType.Type;
import xyz.ukrainskiys.telegrambotstarter.utils.PengradUtils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

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
				if (update == null) {
					continue;
				}

				Long chatId = PengradUtils.getChatId(update);

				Set<String> spentHandlers = new HashSet<>();

				if (update.message() != null) {
					String text = update.message().text();
					if (handlersStorage.isCommands(text)) {
						handlersStorage.getCommandHandler(text)
							.ifPresent(handler -> execute(update, chatId, handler));
						return UpdatesListener.CONFIRMED_UPDATES_ALL;
					} else {
						execute(Type.MESSAGE, update, chatId, spentHandlers);
					}
				}

				execute(Type.ALL, update, chatId, spentHandlers);

				if (update.callbackQuery() != null) {
					execute(Type.CALL_BACK_QUERY, update, chatId, spentHandlers);
				}
				if (update.channelPost() != null) {
					execute(Type.CHANNEL_POST, update, chatId, spentHandlers);
				}
				if (update.chatJoinRequest() != null) {
					execute(Type.CHAT_JOIN_REQUEST, update, chatId, spentHandlers);
				}
				if (update.chosenInlineResult() != null) {
					execute(Type.CHOSEN_INLINE_RESULT, update, chatId, spentHandlers);
				}
				if (update.editedMessage() != null) {
					execute(Type.EDITED_MESSAGE, update, chatId, spentHandlers);
				}
				if (update.editedChannelPost() != null) {
					execute(Type.EDITED_CHANNEL_POST, update, chatId, spentHandlers);
				}
			}

			return UpdatesListener.CONFIRMED_UPDATES_ALL;
		});
	}

	private void execute(Type type, Update update, Long chatId, Set<String> spentHandlers) {
		handlersStorage.getHandlers(type)
			.ifPresent(tHandlers -> tHandlers.forEach(h -> {
				String simpleName = h.getClass().getSimpleName();
				if (!spentHandlers.contains(simpleName)) {
					spentHandlers.add(simpleName);
					CompletableFuture
						.supplyAsync(() -> h.handle(update, chatId))
						.thenApply(bot::execute);
				}
			}));
	}

	private void execute(Update update, Long chatId, TCommandHandler handler) {
		CompletableFuture.supplyAsync(() -> handler.handle(update, Objects.requireNonNull(chatId)))
			.thenApply(bot::execute);
	}
}
