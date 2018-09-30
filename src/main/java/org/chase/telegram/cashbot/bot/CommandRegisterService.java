package org.chase.telegram.cashbot.bot;

import lombok.extern.slf4j.Slf4j;
import org.chase.telegram.cashbot.commands.CashCommand;
import org.chase.telegram.cashbot.commands.DisableCommand;
import org.chase.telegram.cashbot.commands.EnableCommand;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;

import javax.inject.Inject;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Service
@Slf4j
public class CommandRegisterService {
    private final List<CashCommand> cashCommands;

    @Inject
    public CommandRegisterService(final List<CashCommand> cashCommands) {
        this.cashCommands = requireNonNull(cashCommands, "cashCommands");

    }

    void registerCommands(TelegramLongPollingCommandBot bot, IBotCommand... aditionalCommands) {
        log.info("Registering commands...");
        for(IBotCommand command : aditionalCommands) {
            register(bot, command);
        }

        for (CashCommand cashCommand : cashCommands) {
            if (cashCommand.getClass().isAnnotationPresent(EnableCommand.class)) {
                register(bot, cashCommand);
            } else {
                if (!cashCommand.getClass().isAnnotationPresent(DisableCommand.class)) {
                    log.info("Command \"{}\" not enabled", cashCommand.getClass().getSimpleName());
                }
            }
        }

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
