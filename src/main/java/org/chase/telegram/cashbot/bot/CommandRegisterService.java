package org.chase.telegram.cashbot.bot;

import org.chase.telegram.cashbot.commands.StartCommand;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;

import javax.inject.Inject;

@Service
public class CommandRegisterService {
    private final StartCommand startCommand;

    @Inject
    public CommandRegisterService(StartCommand startCommand) {
        this.startCommand = startCommand;
    }

    void registerCommands(TelegramLongPollingCommandBot bot, IBotCommand... aditionalCommands) {
        bot.registerAll(aditionalCommands);
        bot.registerAll(
                startCommand
        );
    }
}
