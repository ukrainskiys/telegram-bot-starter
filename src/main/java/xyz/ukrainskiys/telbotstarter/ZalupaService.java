package xyz.ukrainskiys.telbotstarter;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ZalupaService {

	public String zalupa() {
//		new TelegramBot("").setUpdatesListener();
		return "zalupa";
	}

	public BaseRequest<?, ?> f(BigDecimal decimal) {
		return new SendMessage(123, "qqq");
	}
}
