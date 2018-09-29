package org.chase.telegram.cashbot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupUserIdentifier implements Serializable {
    private long groupId;
    private int userId;
}
