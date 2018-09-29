package org.chase.telegram.cashbot.Account;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "cashaccount")
@Table(name = "cashbot_acccount")
@Data
@AllArgsConstructor
public class AccountEntity {
    @EmbeddedId
    AccountIdentity accountIdentity;

    public AccountEntity(final long groupId, final int userId, final int balance) {
        this.accountIdentity = new AccountIdentity(groupId, userId);
        this.balance = balance;
    }

    private int balance;
}
