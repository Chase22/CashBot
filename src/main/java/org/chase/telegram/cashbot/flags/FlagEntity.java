package org.chase.telegram.cashbot.flags;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "cashflag")
@Table(name = "cashbot_flag")
@AllArgsConstructor
@Data
public class FlagEntity {

    @EmbeddedId
    FlagIdentifier flagIdentifier;

    private Flag flag;

    public FlagEntity(long groupId, int userId, final Flag flag) {
        this.flagIdentifier = new FlagIdentifier(groupId, userId);
        this.flag = flag;
    }
}
