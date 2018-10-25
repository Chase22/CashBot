package org.chase.telegram.cashbot.session;

import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity(name = "cashbot_session")
public class SessionEntity implements Session {

    @Id
    private Integer id;
    private Date startTimeStamp;
    private Date lastAccessTime;

    @Override
    public Serializable getId() {
        return id;
    }

    @Override
    public Date getStartTimestamp() {
        return startTimeStamp;
    }

    @Override
    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    @Override
    public long getTimeout() throws InvalidSessionException {
        return 0;
    }

    @Override
    public void setTimeout(final long maxIdleTimeInMillis) throws InvalidSessionException {

    }

    @Override
    public String getHost() {
        return null;
    }

    @Override
    public void touch() throws InvalidSessionException {

    }

    @Override
    public void stop() throws InvalidSessionException {

    }

    @Override
    public Collection<Object> getAttributeKeys() throws InvalidSessionException {
        return null;
    }

    @Override
    public Object getAttribute(final Object key) throws InvalidSessionException {
        return null;
    }

    @Override
    public void setAttribute(final Object key, final Object value) throws InvalidSessionException {

    }

    @Override
    public Object removeAttribute(final Object key) throws InvalidSessionException {
        return null;
    }
}
