package xyz.ukrainskiys.telegrambotstarter.utils;

import com.pengrad.telegrambot.model.Update;

import java.util.Optional;

public class PengradUtils {
	public static Long getChatId(Update update) {
		if (update.message() != null) {
			return update.message().chat().id();
		} else if (update.callbackQuery() != null) {
			return update.callbackQuery().message().chat().id();
		} else if (update.channelPost() != null) {
			return update.channelPost().chat().id();
		} else if (update.chatJoinRequest() != null) {
			return update.chatJoinRequest().chat().id();
		} else if (update.editedChannelPost() != null) {
			return update.editedChannelPost().chat().id();
		} else if (update.editedMessage() != null) {
			return update.editedMessage().chat().id();
		} else {
			return null;
		}
	}

	public static Optional<String> getText(Update update) {
		if (update.message() != null) {
			return Optional.of(update.message().text());
		} else {
			return Optional.empty();
		}
	}
}
