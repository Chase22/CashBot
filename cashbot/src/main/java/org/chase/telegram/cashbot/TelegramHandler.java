package org.chase.telegram.cashbot;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

public class TelegramHandler extends Handler {
	
	public final String PATTERN = "%4$s: %5$s [%1$tF %1$tT]%n";
	
	private AbsSender sender;
	private long chatID;
	private ConfigurableFormatter format = new ConfigurableFormatter(PATTERN);
	
	public TelegramHandler(AbsSender sender, long ChatID) {
		this.sender = sender;
		this.chatID = ChatID;
	}

	@Override
	public void publish(LogRecord record) {
		SendMessage error = new SendMessage();
		error.setChatId(chatID);
		
		if (record.getThrown() != null) {
			error.setText(format.format(record));
		} else {
			error.setText(format.format(record));
		}
		
		try {
			sender.execute(error);
		} catch (TelegramApiException e) {
			BotLogger.error(this.getClass().getSimpleName(), e);
		}
	}

	@Override
	public void flush() {
		
	}

	@Override
	public void close() throws SecurityException {

	}

}
