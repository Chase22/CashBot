package org.chase.telegram.cashbot.commands.misc;

import org.chase.telegram.cashbot.Changelog;
import org.chase.telegram.cashbot.Messages;
import org.telegram.telegrambots.api.methods.ParseMode;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commandbot.commands.CheckableBotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

public class AboutCommand extends CheckableBotCommand {
	private static final String IDENTIFIER = "about"; //$NON-NLS-1$
	private static final String DESCRIPTION = Messages.getString("AboutCommand.1"); //$NON-NLS-1$
	
	public AboutCommand() {
		super(IDENTIFIER, DESCRIPTION);
	}
	
	@Override
	public void executeCommand(AbsSender absSender, User user, Chat chat, Message message, String[] arguments) {
		SendMessage reply = new SendMessage();
		reply.setChatId(chat.getId());
		reply.setParseMode(ParseMode.MARKDOWN);
		reply.setText(Messages.getFormatString("AboutCommand.2", Changelog.getLatestVersion()));
		System.out.println(reply.getText());
		
		try {
			absSender.execute(reply);
		} catch (TelegramApiException e) {
			BotLogger.error(this.getClass().getSimpleName(), e);
		}
	}

}
