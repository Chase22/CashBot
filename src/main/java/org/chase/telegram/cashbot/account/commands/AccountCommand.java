package org.chase.telegram.cashbot.account.commands;

import org.chase.telegram.cashbot.commands.CashCommand;
import org.chase.telegram.cashbot.commands.HelpCategory;

import java.util.Collections;
import java.util.Set;

public abstract class AccountCommand extends CashCommand {
    public AccountCommand(final String commandIdentifier, final String description, final String extendedDescription) {
        super(commandIdentifier, description, extendedDescription);
    }

    @Override
    public Set<HelpCategory> getCategory() {
        return Collections.singleton(HelpCategory.Account);
    }
}
