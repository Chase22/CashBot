package org.chase.telegram.cashbot;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.chase.telegram.cashbot.bot.CashBot;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.generics.BotSession;
import org.telegram.telegrambots.logging.BotLogger;

public class Main {

	public static BotSession cashBotSession;

	public static void main(String[] args) {
		ApiContextInitializer.init();
		TelegramBotsApi api = new TelegramBotsApi();

		CashBot bot = new CashBot();
		Logger.getGlobal().setLevel(Level.WARNING);
		Logger.getGlobal().addHandler(new TelegramHandler(bot, 188215327));

		try {
			BotLogger.registerLogger(new TelegramHandler(bot, 188215327));
			BotLogger.registerLogger(new ConsoleHandler());
			BotLogger.registerLogger(new FileHandler());
			BotLogger.info(bot.getClass().getSimpleName(), "Bot started");

			cashBotSession = api.registerBot(bot);
		} catch (TelegramApiRequestException | SecurityException | IOException e) {
			BotLogger.error(Main.class.getSimpleName(), e);
			
		}
	}

}
