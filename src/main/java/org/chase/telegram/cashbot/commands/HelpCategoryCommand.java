package org.chase.telegram.cashbot.commands;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.chase.telegram.cashbot.cashUser.CashUserService;
import org.chase.telegram.cashbot.commands.anotations.EnableCommand;
import org.chase.telegram.cashbot.session.SessionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;
import java.util.stream.Collectors;

@EnableCommand
@Component
@Slf4j
public class HelpCategoryCommand extends CashCommand {

    private static final String COMMAND_IDENTIFIER = "help";
    private static final String COMMAND_DESCRIPTION = "shows all commands. Use /help [command] for more info";
    private static final String EXTENDED_DESCRIPTION = "This command displays all commands the bot has to offer.\n /help [command] can display deeper information";

    private final Map<HelpCategory, Set<CashCommand>> commands;
    private final CashUserService cashUserService;
    private final SessionService sessionService;

    public HelpCategoryCommand(List<CashCommand> commandList, final CashUserService cashUserService, final SessionService sessionService) {
        super(COMMAND_IDENTIFIER, COMMAND_DESCRIPTION, EXTENDED_DESCRIPTION);
        commands = getCommandMap(commandList);
        this.cashUserService = cashUserService;
        this.sessionService = sessionService;
    }

    private Map<HelpCategory, Set<CashCommand>> getCommandMap(final List<CashCommand> commandList) {
        Map<HelpCategory, Set<CashCommand>> categorySetMap = Maps.newHashMap();
        commandList.forEach(cashCommand -> cashCommand.getCategory().forEach(helpCategory -> {
            if (!categorySetMap.containsKey(helpCategory)) {
                categorySetMap.put(helpCategory, Sets.newHashSet());
            }
            log.info("Add command {} to category {}", cashCommand.getCommandIdentifier(), helpCategory);
            categorySetMap.get(helpCategory).add(cashCommand);
        }));
        return categorySetMap;
    }

    @Override
    public Optional<CashBotReply> executeCommand(final AbsSender absSender, final Message message, final String[] arguments, Session session) {
        final String helpCategory = (String) session.getAttribute("callbackQueryData");
        if (helpCategory != null) {
            try {
                Set<CashCommand> commandSet = commands.get(HelpCategory.valueOf(helpCategory));
                sessionService.answerCallback(absSender, session);
                return Optional.of(new CashBotReply(message.getChatId(), getHelpText(commandSet)));
            } catch (IllegalArgumentException e) {
                log.error("Invalid Help Category[{}] selected", helpCategory);
                return Optional.of(new CashBotReply(message.getChatId(), "Invalid Help Category selected"));
            } catch (TelegramApiException e) {
                log.error("Error answering Help Callback", e);
                return Optional.empty();
            }
        } else {
            return cashUserService.getById(message.getFrom().getId()).map(cashUser -> {
                CashBotReply reply = new CashBotReply(cashUser.getChatId(), "Please select a help Category");
                reply.setOriginChat(message.getChatId());
                reply.getReply().setReplyMarkup(getReplyMarkup());
                session.setAttribute("activeCommand", COMMAND_IDENTIFIER);
                return Optional.of(reply);
            }).orElse(Optional.of(new CashBotReply(message.getChatId(), "Please send /start to the bot in a private chat")));
        }
    }

    private ReplyKeyboard getReplyMarkup() {
        return new InlineKeyboardMarkup().setKeyboard(
                Lists.partition(commands
                        .keySet()
                        .stream()
                        .map(helpCategory ->
                                new InlineKeyboardButton(helpCategory.name())
                                        .setCallbackData(helpCategory.name())
                        )
                        .collect(Collectors.toList()), 2)
        );
    }

    @Override
    public Optional<CashBotReply> executeCommand(final AbsSender absSender, final Message message, final String[] arguments) {
        return Optional.empty();
    }

    private static String getHelpText(Set<CashCommand> botCommands) {
        StringJoiner reply = new StringJoiner(System.lineSeparator() + System.lineSeparator());
        botCommands.stream().filter(cashCommand -> cashCommand.getClass().isAnnotationPresent(EnableCommand.class))
                .map(com ->
                new StringJoiner(System.lineSeparator())
                .add(COMMAND_INIT_CHARACTER + com.getCommandIdentifier())
                .add("-----------------")
                .add(com.getExtendedDescription()).toString()).forEach(reply::add);
        return reply.toString();
    }
}
