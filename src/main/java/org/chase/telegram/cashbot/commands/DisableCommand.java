package org.chase.telegram.cashbot.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
/**
 * Explicitly disable a command to not be registered with the bot. This also suppresses the warning that the command is not registered
 */
public @interface DisableCommand {
}
