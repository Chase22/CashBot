package org.chase.telegram.cashbot.Account;

import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "cashaccount")
@Table(name = "cashbot_acccount")
@Data
public class AccountEntity {
    @EmbeddedId
    AccountIdentity accountIdentity;

    private int balance;
}
