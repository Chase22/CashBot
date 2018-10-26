package org.chase.telegram.cashbot.cashUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.User;

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

    public CashUser(User user) {
        userId = user.getId();
        this.chatId = user.getId();
        username = user.getUserName();
        firstName = user.getFirstName();
        lastName = user.getLastName();
    }


    public CashUserEntity toEntity() {
        return new CashUserEntity(userId, chatId, username, firstName, lastName);
    }

    public String getDisplayName() {
        String name = firstName;
        if (lastName != null) {
            name += " " + lastName;
        }

        if (name == null || name.isEmpty()) {
            name = username;
        }

        return name;
    }
}
