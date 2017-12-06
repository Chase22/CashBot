package org.chase.telegram.cashbot.bot;

import javax.persistence.EntityManager;

import org.chase.telegram.cashbot.commands.StartCommand;
import org.chase.telegram.cashbot.commands.StopCommand;
import org.chase.telegram.cashbot.commands.account.AddToAccountCommand;
import org.chase.telegram.cashbot.commands.account.OpenAccountCommand;
import org.chase.telegram.cashbot.commands.account.SetAccountToCommand;
import org.chase.telegram.cashbot.commands.account.ShowAccountCommand;
import org.chase.telegram.cashbot.commands.account.ShowAllAccountsCommand;
import org.chase.telegram.cashbot.commands.account.TransferToAccountCommand;
import org.chase.telegram.cashbot.commands.config.ConfigSetCommand;
import org.chase.telegram.cashbot.commands.config.ConfigShowCommand;
import org.chase.telegram.cashbot.commands.misc.AboutCommand;
import org.chase.telegram.cashbot.commands.misc.ChangelogCommand;
import org.chase.telegram.cashbot.commands.misc.HelpCommand;
import org.chase.telegram.cashbot.data.Account;
import org.chase.telegram.cashbot.data.CashChat;
import org.chase.telegram.cashbot.data.CashUser;
import org.chase.telegram.cashbot.data.EntityUtility;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.commandbot.TelegramLongPollingCommandBot;

public class CashBot extends TelegramLongPollingCommandBot {

	//public final static String TOKEN = "472345994:AAHnW06NymPiMK8tvPBayz2TtGL_hAwS9jE"; //$NON-NLS-1$
	public final static String TOKEN = "340581395:AAFtbKSPPt25sCRiPWg7fP66o_zE1U9U-aA"; //$NON-NLS-1$
	public final static String USERNAME = "rpcashbot"; //$NON-NLS-1$

	/**
	 * @param options
	 * @param allowCommandsWithUsername
	 * @param botUsername
	 */
	public CashBot(DefaultBotOptions options, boolean allowCommandsWithUsername) {
		super(options, allowCommandsWithUsername, USERNAME);
		registerCommands();
	}

	/**
	 * @param options
	 * @param botUsername
	 */
	public CashBot(DefaultBotOptions options) {
		super(options, USERNAME);
		registerCommands();
	}

	/**
	 * @param botUsername
	 */
	public CashBot() {
		super(USERNAME);
		registerCommands();
	}

	private void registerCommands() {
		register(new HelpCommand());
		register(new StartCommand());
		register(new StopCommand());
		register(new ConfigShowCommand());
		register(new OpenAccountCommand());
		register(new ShowAccountCommand());
		register(new ShowAllAccountsCommand());
		register(new AddToAccountCommand());
		register(new SetAccountToCommand());
		register(new TransferToAccountCommand());
		register(new AboutCommand());
		register(new ChangelogCommand());
		
		for (CashChat.Configs value : CashChat.Configs.values()) {
			register(new ConfigSetCommand(value));
		}
	}

	@Override
	public void processNonCommandUpdate(Update update) {

		if (update.hasMessage()) {
			Message message = update.getMessage();
			if (message.isGroupMessage() || message.isSuperGroupMessage()) {
				System.out.println(message.getText());
				EntityManager manager = EntityUtility.getEntityManager();
				CashUser user = CashUser.load(manager, message.getFrom().getId());
				if (user != null) {
					user.update(manager, message.getFrom().getUserName(), message.getFrom().getFirstName(),
							message.getFrom().getLastName());

					Account account = Account.load(manager, user.getID(), message.getChatId());
					CashChat chat = CashChat.load(manager, message.getChatId());

					if (account != null) {
						if (message.hasText()) {
							account.modifyBalance(manager, chat.getAmountText());
							System.out.println(account.getBalance());
						} else if (message.hasPhoto()) {
							account.modifyBalance(manager, chat.getAmountPic());
							System.out.println(account.getBalance());
						} else if (message.getVoice() != null) {
							account.modifyBalance(manager, chat.getAmountVoice());
							System.out.println(account.getBalance());
						} else if (message.getSticker() != null) {
							account.modifyBalance(manager, chat.getAmountSticker());
							System.out.println(account.getBalance());
						}
					}
				}

			}
		}
	}

	@Override
	public String getBotToken() {
		return TOKEN;
	}

}
