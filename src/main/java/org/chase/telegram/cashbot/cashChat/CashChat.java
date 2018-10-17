package org.chase.telegram.cashbot.cashChat;

import lombok.AllArgsConstructor;
import lombok.Data;

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
}
