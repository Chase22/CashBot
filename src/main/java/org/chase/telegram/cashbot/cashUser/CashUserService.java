package org.chase.telegram.cashbot.cashUser;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CashUserService {
    private final CashUserRepository cashUserRepository;

    public CashUserService(final CashUserRepository cashUserRepository) {
        this.cashUserRepository = Objects.requireNonNull(cashUserRepository, "cashUserRepository");
    }

    public Optional<CashUser> getById(int userId) {
        return cashUserRepository.findByUserId(userId).map(CashUser::new);
    }

    public List<CashUser> getForChatId(String chatId) {
        return null;
    }

    public CashUserEntity save(final CashUser cashUser) {
        return cashUserRepository.save(cashUser.toEntity());
    }

    public Optional<CashUser> getByUsername(final String username) {
        final String usernameSanitized = username.replace("@", "").toLowerCase();

        return cashUserRepository.findByUsernameIgnoreCase(usernameSanitized).map(CashUser::new);
    }
}
