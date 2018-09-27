package org.chase.telegram.cashbot.CashUser;

import lombok.Data;

@Data
public class CashUser {
    private long userId;
    private String chatId;
    private String username;
    private String firstName;
    private String lastName;

    public CashUser(CashUserEntity cashUserEntity) {
        userId = cashUserEntity.getUserId();
        chatId = cashUserEntity.getChatId();
        username = cashUserEntity.getUsername();
        firstName = cashUserEntity.getFirstName();
        lastName = cashUserEntity.getLastName();
    }
}
