package org.chase.telegram.cashbot.session;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.chase.telegram.cashbot.GroupUserIdentifier;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity(name = "cashbot_session")
@Table(name = "cashbot_session")
@Data
@AllArgsConstructor
public class SessionEntity {

    @EmbeddedId
    @Setter(AccessLevel.NONE)
    GroupUserIdentifier groupUserIdentifier;

    private LocalDateTime lastAccessed;

    private String activeCommand;

    private String callbackQueryId;
    private String callbackQueryData;
    private int callbackQueryMessageId;
    private long callbackQueryChatId;

    public SessionEntity(final long groupId, final int userId) {
        this.groupUserIdentifier = new GroupUserIdentifier(groupId, userId);
    }
}
