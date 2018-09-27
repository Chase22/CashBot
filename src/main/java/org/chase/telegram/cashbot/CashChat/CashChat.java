package org.chase.telegram.cashbot.CashChat;

import lombok.Data;

@Data
public class CashChat {
    private String ChatID;
    private int startAmount;
    private int amountText;
    private int amountPic;
    private int amountVoice;
    private int amountSticker;
    private String currencyName;

    public CashChat(final CashChatEntity cashChatEntity) {
        ChatID = cashChatEntity.getChatID();
        startAmount = cashChatEntity.getStartAmount();
        amountPic = cashChatEntity.getAmountPic();
        amountSticker = cashChatEntity.getAmountPic();
        amountText = cashChatEntity.getAmountText();
        amountVoice = cashChatEntity.getAmountVoice();
    }
}
