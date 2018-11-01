package org.chase.telegram.cashbot.session;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

import static java.time.ZoneOffset.UTC;

@AllArgsConstructor
public class Session {

    private long groupId;
    private int userId;
    private LocalDateTime lastAccessed;

    private String activeCommand;

    private String callbackQueryId;
    private String callbackQueryData;
    private int callbackQueryMessageId;
    private long callbackQueryChatId;

    public Session(final long groupId, final int userId) {
        this.groupId = groupId;
        this.userId = userId;
        this.touch();
    }

    public Session(final SessionEntity entity) {
        this.groupId = entity.getGroupUserIdentifier().getGroupId();
        this.userId = entity.getGroupUserIdentifier().getUserId();
        this.lastAccessed = entity.getLastAccessed();
        this.activeCommand = entity.getActiveCommand();
        this.callbackQueryId = entity.getCallbackQueryId();
        this.callbackQueryData = entity.getCallbackQueryData();
        this.callbackQueryMessageId = entity.getCallbackQueryMessageId();
        this.callbackQueryChatId = entity.getCallbackQueryChatId();
    }

    public SessionEntity toEntity() {
        SessionEntity entity = new SessionEntity(groupId, userId);

        entity.setLastAccessed(lastAccessed);

        entity.setActiveCommand(activeCommand);
        entity.setCallbackQueryChatId(callbackQueryChatId);
        entity.setCallbackQueryData(callbackQueryData);
        entity.setCallbackQueryId(callbackQueryId);
        entity.setCallbackQueryMessageId(callbackQueryMessageId);

        return entity;
    }

    private void touch() {
        lastAccessed = LocalDateTime.now(UTC);
    }

    public long getGroupId() {
        touch();
        return groupId;
    }

    public void setGroupId(final long groupId) {
        touch();
        this.groupId = groupId;
    }

    public int getUserId() {
        touch();
        return userId;
    }

    public LocalDateTime getLastAccessed() {
        touch();
        return lastAccessed;
    }

    public String getActiveCommand() {
        touch();
        return activeCommand;
    }

    public void setActiveCommand(final String activeCommand) {
        touch();
        this.activeCommand = activeCommand;
    }

    public String getCallbackQueryId() {
        touch();
        return callbackQueryId;
    }

    public void setCallbackQueryId(final String callbackQueryId) {
        touch();
        this.callbackQueryId = callbackQueryId;
    }

    public String getCallbackQueryData() {
        touch();
        return callbackQueryData;
    }

    public void setCallbackQueryData(final String callbackQueryData) {
        touch();
        this.callbackQueryData = callbackQueryData;
    }

    public int getCallbackQueryMessageId() {
        touch();
        return callbackQueryMessageId;
    }

    public void setCallbackQueryMessageId(final int callbackQueryMessageId) {
        touch();
        this.callbackQueryMessageId = callbackQueryMessageId;
    }

    public long getCallbackQueryChatId() {
        touch();
        return callbackQueryChatId;
    }

    public void setCallbackQueryChatId(final long callbackQueryChatId) {
        touch();
        this.callbackQueryChatId = callbackQueryChatId;
    }
}
