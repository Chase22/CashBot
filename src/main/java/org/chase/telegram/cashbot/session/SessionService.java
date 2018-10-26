package org.chase.telegram.cashbot.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.session.ChatIdConverter;
import org.telegram.telegrambots.session.DefaultChatIdConverter;
import org.telegram.telegrambots.session.DefaultChatSessionContext;

import java.util.Optional;

@Service
public class SessionService {

    private final DefaultSessionManager sessionManager;
    private final ChatIdConverter chatIdConverter;

    public SessionService() {
        sessionManager = new DefaultSessionManager();
        chatIdConverter = new DefaultChatIdConverter();
        AbstractSessionDAO sessionDAO = (AbstractSessionDAO) sessionManager.getSessionDAO();
        sessionDAO.setSessionIdGenerator(chatIdConverter);
    }

    public Optional<Session> getSession(Message message){
        SessionContext botSession = new DefaultChatSessionContext(message.getChatId(), message.getFrom().getUserName());
        try {
            return Optional.of(Optional.of(sessionManager.getSession(chatIdConverter)).orElse(sessionManager.start(botSession)));
        } catch (UnknownSessionException e) {
            return Optional.ofNullable(sessionManager.start(botSession));
        }
    }

    public void answerCallback(AbsSender sender, Session session) throws TelegramApiException {
        session.removeAttribute("callbackQueryData");
        session.removeAttribute("activeCommand");
        sender.execute(new AnswerCallbackQuery().setCallbackQueryId((String) session.removeAttribute("callbackQueryId")));
        sender.execute(new DeleteMessage(
                ((long) session.removeAttribute("callbackQueryChatId")),
                ((int) session.removeAttribute("callbackQueryMessageId"))));
    }

    public void answerCallback(AbsSender sender, Session session, String text) throws TelegramApiException {
        session.removeAttribute("callbackQueryData");
        session.removeAttribute("activeCommand");
        sender.execute(new AnswerCallbackQuery().setCallbackQueryId((String) session.removeAttribute("callbackQueryId")).setText(text));
    }
}
