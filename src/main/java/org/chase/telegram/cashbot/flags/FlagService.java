package org.chase.telegram.cashbot.flags;

import org.chase.telegram.cashbot.GroupUserIdentifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Service
public class FlagService {
    private final FlagRepository flagRepository;

    public FlagService(final FlagRepository flagRepository) {
        this.flagRepository = requireNonNull(flagRepository,"flagRepository");
    }

    public Optional<Flag> getFlagForGroupAndUser(long groupId, int userId) {
        return flagRepository.getByGroupUserIdentifier(new GroupUserIdentifier(groupId, userId)).map(FlagEntity::getFlag);
    }

    public boolean userHasFlagInGroup(long groupId, int userId, Flag expectedFlag) {
        Optional<Flag> optionalFlag = getFlagForGroupAndUser(groupId, userId);

        return optionalFlag.map(actualFlag -> actualFlag.equals(expectedFlag)).orElse(false);
    }

    public void setFlag(long groupId, int userId, Flag flag) {
        FlagEntity flagEntity = new FlagEntity(groupId, userId, flag);
        flagRepository.save(flagEntity);
    }
}
