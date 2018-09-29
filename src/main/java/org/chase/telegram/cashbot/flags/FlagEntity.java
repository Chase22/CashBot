package org.chase.telegram.cashbot.flags;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.chase.telegram.cashbot.GroupUserIdentifier;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "cashflag")
@Table(name = "cashbot_flag")
@AllArgsConstructor
@Data
public class FlagEntity {

    @EmbeddedId
    GroupUserIdentifier groupUserIdentifier;

    private Flag flag;

    public FlagEntity(long groupId, int userId, final Flag flag) {
        this.groupUserIdentifier = new GroupUserIdentifier(groupId, userId);
        this.flag = flag;
    }
}
