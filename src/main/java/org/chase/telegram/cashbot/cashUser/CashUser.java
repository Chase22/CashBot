package org.chase.telegram.cashbot.cashUser;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CashUser {
    private int userId;
    private long chatId;
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

    public CashUserEntity toEntity() {
        return new CashUserEntity(userId, chatId, username, firstName, lastName);
    }
}
