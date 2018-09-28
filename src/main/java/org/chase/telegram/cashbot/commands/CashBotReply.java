package org.chase.telegram.cashbot.commands;

import lombok.Data;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Data
public
class CashBotReply{
    private long chatId;
    private String message;

    public CashBotReply(long chatId, String message, Object... args) {
        this.message = String.format(message, args);
        this.chatId = chatId;
    }

    void sendMessage(AbsSender sender) throws TelegramApiException {
        sender.execute(new SendMessage(chatId, getMessage()));
    }
}
