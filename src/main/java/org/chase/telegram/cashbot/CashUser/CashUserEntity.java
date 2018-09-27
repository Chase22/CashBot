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
    private long userID;
    private String chatID;
    private String username;
    private String firstName;
    private String lastName;
}
