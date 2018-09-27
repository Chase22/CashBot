package org.chase.telegram.cashbot.CashChat;

import lombok.Data;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Data
public class CashBotReply{
    private String chatId;
    private String message;

    public CashBotReply(String message, String chatId, Object... args) {
        this.message = String.format(message, args);
        this.chatId = chatId;
    }

    public Message sendMessage(AbsSender sender) throws TelegramApiException {
        return sender.execute(new SendMessage(getMessage(), chatId));
    }
}
