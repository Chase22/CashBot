package org.chase.telegram.cashbot.commands;

import lombok.Data;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Data
public
class CashBotReply{
    private SendMessage reply;

    public CashBotReply(long chatId, String message, Object... args) {
        reply = new SendMessage(chatId, String.format(message, args));
    }

    public CashBotReply(long chatId) {
        reply = new SendMessage();
        reply.setChatId(chatId);
    }

    void sendMessage(AbsSender sender) throws TelegramApiException {
        sender.execute(reply);
    }
}
