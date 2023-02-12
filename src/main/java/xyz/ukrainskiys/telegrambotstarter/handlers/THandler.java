package xyz.ukrainskiys.telegrambotstarter.handlers;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;

public interface THandler {
    BaseRequest<?, ?> execute(Update update);
}
