package org.chase.telegram.cashbot.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;

import java.io.Serializable;
import java.util.Collection;

public class DatabaseSessionDAO extends AbstractSessionDAO {
    @Override
    protected Serializable doCreate(final Session session) {
        return null;
    }

    @Override
    protected Session doReadSession(final Serializable sessionId) {
        return null;
    }

    @Override
    public void update(final Session session) throws UnknownSessionException {

    }

    @Override
    public void delete(final Session session) {

    }

    @Override
    public Collection<Session> getActiveSessions() {
        return null;
    }
}
