package org.chase.telegram.cashbot.Account;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.chase.telegram.cashbot.GroupUserIdentifier;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "cashaccount")
@Table(name = "cashbot_acccount")
@Data
@AllArgsConstructor
public class AccountEntity {
    @EmbeddedId
    GroupUserIdentifier groupUserIdentifier;

    public AccountEntity(final long groupId, final int userId, final int balance) {
        this.groupUserIdentifier = new GroupUserIdentifier(groupId, userId);
        this.balance = balance;
    }

    private int balance;
}
