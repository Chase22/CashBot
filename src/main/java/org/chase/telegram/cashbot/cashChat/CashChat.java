package org.chase.telegram.cashbot.cashChat;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.StringJoiner;

@Data
@AllArgsConstructor
public class CashChat {

    private long chatId;
    private String title;
    private int startAmount;
    private int amountText;
    private int amountPic;
    private int amountVoice;
    private int amountSticker;
    private int amountOther;
    private String currencyName;

    CashChat(final CashChatEntity cashChatEntity) {
        chatId = cashChatEntity.getChatId();
        title = cashChatEntity.getTitle();
        startAmount = cashChatEntity.getStartAmount();
        amountPic = cashChatEntity.getAmountPic();
        amountSticker = cashChatEntity.getAmountPic();
        amountText = cashChatEntity.getAmountText();
        amountVoice = cashChatEntity.getAmountVoice();
        amountOther = cashChatEntity.getAmountOther();
        currencyName = cashChatEntity.getCurrencyName();
    }

    public CashChatEntity toEntity() {
        return new CashChatEntity(
                chatId,
                title,
                startAmount,
                amountText,
                amountPic,
                amountVoice,
                amountSticker,
                amountOther,
                currencyName
        );
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("chatId", chatId)
                .append("title", title)
                .append("startAmount", startAmount)
                .append("amountText", amountText)
                .append("amountPic", amountPic)
                .append("amountVoice", amountVoice)
                .append("amountSticker", amountSticker)
                .append("amountOther", amountOther)
                .append("currencyName", currencyName)
                .toString();
    }

    public String toChatString() {
        return new StringJoiner("\n")
                .add("currencyname: " + currencyName)
                .add("startamount: " + startAmount)
                .add("amount text: " + amountText)
                .add("amount picture: " + amountPic)
                .add("amount voice: " + amountVoice)
                .add("amount sticker: " + amountSticker)
                .add("amount other: " + amountOther)
                .toString();
    }
}
