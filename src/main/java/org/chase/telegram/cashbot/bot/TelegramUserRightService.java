package org.chase.telegram.cashbot.bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatMember;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.ChatMember;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;

@Component
public class TelegramUserRightService {
	public enum GroupRight {
		CHANGE_INFORMATION, DELETE_MESSAGES, EDIT_MESSAGES, INVITE_USERS, PIN_MESSAGES, POST_MESSAGES, PROMOTE_MEMBERS, RESTRICT_USERS, SEND_MEDIA_MESSAGES, SEND_MESSAGES, SEND_OTHER_MESSAGES
	}

	public boolean isAdministrator(AbsSender absSender, Chat chat, User user) throws TelegramApiException {
		ArrayList<ChatMember> admins = getAdmins(absSender, chat);
		long id = getChatMember(absSender, chat, user).getUser().getId();
		for (ChatMember admin : admins) {
			if (admin.getUser().getId() == id) return true;
		}
		return false;
	}

	public ChatMember getChatMember(AbsSender absSender, Chat chat, User user) throws TelegramApiException {
		GetChatMember gcm = new GetChatMember();
		gcm.setChatId(chat.getId());
		gcm.setUserId(user.getId());
		return absSender.execute(gcm);
	}

	public static ArrayList<ChatMember> getAdmins(AbsSender absSender, Chat chat) throws TelegramApiException {
		GetChatAdministrators gca = new GetChatAdministrators();
		gca.setChatId(chat.getId());
		return absSender.execute(gca);
	}

	public boolean hasRight(AbsSender absSender, Chat chat, User user, GroupRight rights)
			throws TelegramApiException {
		ChatMember member = getChatMember(absSender, chat, user);

		switch (rights) {
		case CHANGE_INFORMATION:
			return member.getCanChangeInformation();
		case DELETE_MESSAGES:
			return member.getCanDeleteMessages();
		case EDIT_MESSAGES:
			return member.getCanEditMessages();
		case INVITE_USERS:
			return member.getCanInviteUsers();
		case PIN_MESSAGES:
			return member.getCanPinMessages();
		case POST_MESSAGES:
			return member.getCanPostMessages();
		case PROMOTE_MEMBERS:
			return member.getCanPromoteMembers();
		case RESTRICT_USERS:
			return member.getCanRestrictUsers();
		case SEND_MEDIA_MESSAGES:
			return member.getCanSendMediaMessages();
		case SEND_MESSAGES:
			return member.getCanSendMessages();
		case SEND_OTHER_MESSAGES:
			return member.getCanSendOtherMessages();
		}
		return false;
	}
	
	public HashMap<GroupRight, Boolean> hasRights(AbsSender absSender, Chat chat, User user, GroupRight... rights) throws TelegramApiException {
		HashMap<GroupRight, Boolean> rightMap = new HashMap<>();
		for(GroupRight right : rights) {
			rightMap.put(right, hasRight(absSender, chat, user, right));
		}
		return rightMap;
	}
}
