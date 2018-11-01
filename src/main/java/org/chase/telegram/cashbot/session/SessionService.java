package org.chase.telegram.cashbot.session;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.time.ZoneOffset.UTC;

@Service
public class SessionService {

    private static final long SESSION_TIMEOUT = 30;
    private final SessionRepository sessionRepository;

    public SessionService(final SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Session getSession(Message message) {
       return getSession(message.getChatId(), message.getFrom().getId());
    }

    public Session getSession(final long chatId, final int userId) {
        Optional<SessionEntity> entity =
                sessionRepository.findByGroupUserIdentifier_GroupIdAndGroupUserIdentifier_UserId(chatId, userId);
        if (entity.isPresent()) {
            if (Duration.between(entity.get().getLastAccessed(), LocalDateTime.now(UTC)).toMinutes() < SESSION_TIMEOUT) {
                return new Session(entity.get());
            }
        }
        return new Session(chatId, userId);
    }

    public void answerCallback(AbsSender sender, Session session) throws TelegramApiException {
        sender.execute(new DeleteMessage(
                session.getCallbackQueryChatId(),
                session.getCallbackQueryMessageId()));
        delete(session);
    }

    public void delete(final Session session) {
        sessionRepository.delete(session.toEntity());
    }

    public void answerCallback(AbsSender sender, Session session, String text) throws TelegramApiException {
        sender.execute(new AnswerCallbackQuery().setCallbackQueryId(session.getCallbackQueryId()).setText(text));
        delete(session);
    }

    public void save(final Session session) {
        sessionRepository.save(session.toEntity());
    }
}
