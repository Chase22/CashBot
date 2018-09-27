package org.chase.telegram.cashbot.CashChat;

import lombok.Data;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Data
public class CashBotException extends Exception{
    private String chatId;

    public CashBotException(String message, String chatId, Object... args) {
        super(String.format(message, args));
        this.chatId = chatId;
    }

    public CashBotException(String message, Throwable cause, String chatId, Object... args) {
        super(String.format(message, args), cause);
        this.chatId = chatId;
    }

    public CashBotException(Throwable cause, String chatId) {
        super(cause);
        this.chatId = chatId;
    }

    public Message sendError(AbsSender sender) throws TelegramApiException {
        return sender.execute(new SendMessage(getMessage(), chatId));
    }
}
