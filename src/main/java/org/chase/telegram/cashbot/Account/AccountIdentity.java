package org.chase.telegram.cashbot.Account;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
class AccountIdentity implements Serializable {
    private String groupId;
    private int userId;
}