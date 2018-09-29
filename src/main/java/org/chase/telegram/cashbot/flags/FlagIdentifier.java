package org.chase.telegram.cashbot.flags;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
public class FlagIdentifier implements Serializable {
    private long groupId;
    private int userID;
}
