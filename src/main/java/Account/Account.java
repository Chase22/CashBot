package Account;

import lombok.Data;

@Data
public class Account {
    private long groupId;
    private int userId;
    private int balance;

    Account(final AccountEntity cashChatEntity) {
        groupId = cashChatEntity.getAccountIdentity().getGroupId();
        userId = cashChatEntity.getAccountIdentity().getUserId();
        balance = cashChatEntity.getBalance();
    }
}
