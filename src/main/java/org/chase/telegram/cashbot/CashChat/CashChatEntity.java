package org.chase.telegram.cashbot.CashChat;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "cashchat")
@Table(name = "chat")
@Data
public class CashChatEntity {
    @Id
    private String ChatID;
    private int startAmount;
    private int amountText;
    private int amountPic;
    private int amountVoice;
    private int amountSticker;
    private String currencyName;
}
