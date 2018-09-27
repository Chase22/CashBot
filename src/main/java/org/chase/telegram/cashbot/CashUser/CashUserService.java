package org.chase.telegram.cashbot.CashUser;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CashUserService {
    private final CashUserRepository cashUserRepository;

    public CashUserService(final CashUserRepository cashUserRepository) {
        this.cashUserRepository = Objects.requireNonNull(cashUserRepository, "cashUserRepository");
    }

    public CashUser getById(long userId) {
        return new CashUser(cashUserRepository.getOne(userId));
    }

    public List<CashUser> getForChatId(String chatId) {
        return null;
    }
}
