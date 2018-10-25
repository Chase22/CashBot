package org.chase.telegram.cashbot.commands;

import lombok.Data;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Data
public
class CashBotReply{
    private SendMessage reply;
    private Long originChat;

    public CashBotReply(long chatId, String message, Object... args) {
        reply = new SendMessage(chatId, String.format(message, args));
    }

    public CashBotReply(long chatId) {
        reply = new SendMessage();
        reply.setChatId(chatId);
    }

    public void sendMessage(AbsSender sender, final Integer messageId) throws TelegramApiException {

        if (originChat != null) {
            sender.execute(new SendMessage(originChat, "I send it to you in private").setReplyToMessageId(messageId));
        } else {
            reply.setReplyToMessageId(messageId);
        }
        sender.execute(reply);
    }
}
