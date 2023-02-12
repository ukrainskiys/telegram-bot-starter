package xyz.ukrainskiys.telegrambotstarter.handlers;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface TCommandHandler {
    List<String> commands();
    BaseRequest<?, ?> execute(@NotNull Update update, @NotNull Long chatId);
}
