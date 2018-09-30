package org.chase.telegram.cashbot.cashChat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "cashchat")
@Table(name = "cashbot_chat")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CashChatEntity {
    @Id
    private long chatId;
    private String title;
    private int startAmount;
    private int amountText;
    private int amountPic;
    private int amountVoice;
    private int amountSticker;
    private int amountOther;
    private String currencyName;

}
