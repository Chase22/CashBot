package org.chase.telegram.cashbot.bot;

import lombok.extern.slf4j.Slf4j;
import org.chase.telegram.cashbot.Account.commands.CloseAccount;
import org.chase.telegram.cashbot.Account.commands.OpenAccount;
import org.chase.telegram.cashbot.Account.commands.ShowAccounts;
import org.chase.telegram.cashbot.CashUser.commands.ShowMe;
import org.chase.telegram.cashbot.commands.StartCommand;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;

import javax.inject.Inject;

import static java.util.Objects.requireNonNull;

@Service
@Slf4j
public class CommandRegisterService {
    private final StartCommand startCommand;
    private final ShowMe showMe;
    private final OpenAccount openAccount;
    private final ShowAccounts showAccounts;
    private final CloseAccount closeAccount;

    @Inject
    public CommandRegisterService(final StartCommand startCommand, final ShowMe showMe, final OpenAccount openAccount, final ShowAccounts showAccounts, final CloseAccount closeAccount) {
        this.startCommand = requireNonNull(startCommand, "startCommand");
        this.showMe = requireNonNull(showMe, "showMe");
        this.openAccount = requireNonNull(openAccount, "openAccount");
        this.showAccounts = requireNonNull(showAccounts, "showAccounts");
        this.closeAccount = requireNonNull(closeAccount, "closeAccount");
    }

    void registerCommands(TelegramLongPollingCommandBot bot, IBotCommand... aditionalCommands) {
        log.info("Registering commands...");
        for(IBotCommand command : aditionalCommands) {
            register(bot, command);
        }

        register(bot, startCommand);
        register(bot, showMe);
        register(bot, openAccount);
        register(bot, showAccounts);
        register(bot, closeAccount);
        log.info("Finish registering commands");
    }

    private void register(TelegramLongPollingCommandBot bot, final IBotCommand command) {
        if (bot.register(command)) {
            log.info("Registered command: \"{}\"", command.getCommandIdentifier());
        } else {
            log.error("Error registering command: \"{}\"", command.getCommandIdentifier());
        }
    }
}
