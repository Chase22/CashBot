package org.chase.telegram.cashbot.CashUser;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "cashUser")
@Table(name = "cashbot_user")
@Data
public class CashUserEntity{

    @Id
    private long userId;
    private String chatId;
    private String username;
    private String firstName;
    private String lastName;
}
