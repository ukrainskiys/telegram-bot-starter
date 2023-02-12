package xyz.ukrainskiys.telegrambotstarter.handlers;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import org.jetbrains.annotations.NotNull;

public interface THandler {
    BaseRequest<?, ?> execute(@NotNull Update update, Long chatId);
}
