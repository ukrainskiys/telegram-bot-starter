package xyz.ukrainskiys.telegrambotstarter.handlers;

import java.util.List;

public interface TCommandsHandler extends THandler {
    List<String> commands();
}
