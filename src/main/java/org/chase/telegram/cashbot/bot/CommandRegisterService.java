package org.chase.telegram.cashbot.bot;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.bridge.ICommand;
import org.chase.telegram.cashbot.CashUser.commands.ShowMe;
import org.chase.telegram.cashbot.commands.StartCommand;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;

import javax.inject.Inject;

@Service
@Slf4j
public class CommandRegisterService {
    private final StartCommand startCommand;
    private final ShowMe showMe;

    @Inject
    public CommandRegisterService(final StartCommand startCommand, final ShowMe showMe) {
        this.startCommand = startCommand;
        this.showMe = showMe;
    }

    void registerCommands(TelegramLongPollingCommandBot bot, IBotCommand... aditionalCommands) {
        log.info("Registering commands...");
        for(IBotCommand command : aditionalCommands) {
            register(bot, command);
        }

        register(bot, startCommand);
        register(bot, showMe);
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
