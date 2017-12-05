package org.chase.telegram.cashbot.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.chase.telegram.cashbot.Messages;
import org.telegram.telegrambots.logging.BotLogger;

@Entity
@Table(name = "CHAT")
public class CashChat implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3901625163197089961L;
	@Id
	private long ChatID;
	private int startAmount;
	private int amountText;
	private int amountPic;
	private int amountVoice;
	private int amountSticker;
	private String currencyName;

	public enum Configs {
		CurrencyName, InitialBalance, AmountText, AmountSticker, AmountPicture, AmountVoice, AllAmounts
	}

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "groupID")
	private List<Account> accounts = new ArrayList<>();
	
	protected CashChat() {};

	/**
	 * @param chatID
	 */
	private CashChat(long chatID) {
		super();
		ChatID = chatID;
	}
	
	public static CashChat createDefault(EntityManager manager, long ChatID) {
		CashChat newChat = new CashChat(ChatID);
		newChat.amountPic = 10;
		newChat.amountSticker = 10;
		newChat.amountText = 10;
		newChat.amountVoice = 10;
		newChat.currencyName = Messages.getString("CashChat.0"); //$NON-NLS-1$

		try {
			manager.getTransaction().begin();
			manager.persist(newChat);
			manager.getTransaction().commit();

			return newChat;
		} catch (Exception e) {
			BotLogger.error(CashChat.class.getSimpleName(), e);
			manager.getTransaction().rollback();
			return null;
		}

	}

	public static CashChat create(EntityManager manager, long ChatID) {
		CashChat chat = new CashChat(ChatID);

		try {
			manager.getTransaction().begin();
			manager.persist(chat);
			manager.getTransaction().commit();

			return chat;
		} catch (Exception e) {
			BotLogger.error(CashChat.class.getSimpleName(), e);
			manager.getTransaction().rollback();
			return null;
		}
	}

	public static CashChat load(EntityManager manager, long ID) {
		manager.getTransaction().begin();
		CashChat chat = (CashChat) manager.find(CashChat.class, ID);
		manager.getTransaction().commit();
		return chat;
	}

	public static boolean exists(EntityManager manager, long ID) {
		return load(manager, ID) != null;
	}

	/**
	 * @return the chatID
	 */
	public long getChatID() {
		return ChatID;
	}

	/**
	 * @return the startAmount
	 */
	public int getStartAmount() {
		return startAmount;
	}

	/**
	 * @param startAmount
	 *            the startAmount to set
	 */
	public void setStartAmount(EntityManager manager, int startAmount) {
		manager.getTransaction().begin();
		this.startAmount = startAmount;
		manager.getTransaction().commit();
	}

	/**
	 * @return the amountText
	 */
	public int getAmountText() {
		return amountText;
	}

	/**
	 * @param amountText
	 *            the amountText to set
	 */
	public void setAmountText(EntityManager manager, int amountText) {
		manager.getTransaction().begin();
		this.amountText = amountText;
		manager.getTransaction().commit();
	}

	/**
	 * @return the amountPic
	 */
	public int getAmountPic() {
		return amountPic;
	}

	/**
	 * @param amountPic
	 *            the amountPic to set
	 */
	public void setAmountPic(EntityManager manager, int amountPic) {
		manager.getTransaction().begin();
		this.amountPic = amountPic;
		manager.getTransaction().commit();
	}

	/**
	 * @return the amountVoice
	 */
	public int getAmountVoice() {
		return amountVoice;
	}

	/**
	 * @param amountVoice
	 *            the amountVoice to set
	 */
	public void setAmountVoice(EntityManager manager, int amountVoice) {
		manager.getTransaction().begin();
		this.amountVoice = amountVoice;
		manager.getTransaction().commit();
	}

	/**
	 * @return the amountSticker
	 */
	public int getAmountSticker() {
		return amountSticker;
	}

	/**
	 * @param amountSticker
	 *            the amountSticker to set
	 */
	public void setAmountSticker(EntityManager manager, int amountSticker) {
		manager.getTransaction().begin();
		this.amountSticker = amountSticker;
		manager.getTransaction().commit();
	}

	/**
	 * @return the currencyName
	 */
	public String getCurrencyName() {
		return currencyName;
	}

	/**
	 * @param currencyName
	 *            the currencyName to set
	 */
	public void setCurrencyName(EntityManager manager, String currencyName) {
		manager.getTransaction().begin();
		this.currencyName = currencyName;
		manager.getTransaction().commit();
	}

	/**
	 * @return the accounts
	 */
	public List<Account> getAccounts() {
		return accounts;
	}

	public void setConfig(EntityManager manager, Configs config, String value) {
		switch (config) {
		case AmountPicture:
			setAmountPic(manager, Integer.parseInt(value));
			break;
		case AmountSticker:
			setAmountSticker(manager, Integer.parseInt(value));
			break;
		case AmountText:
			setAmountText(manager, Integer.parseInt(value));
			break;
		case AmountVoice:
			setAmountVoice(manager, Integer.parseInt(value));
			break;
		case CurrencyName:
			setCurrencyName(manager, value);
			break;
		case InitialBalance:
			setStartAmount(manager, Integer.parseInt(value));
			break;
		case AllAmounts:
			setAmountPic(manager, Integer.parseInt(value));
			setAmountSticker(manager, Integer.parseInt(value));
			setAmountText(manager, Integer.parseInt(value));
			setAmountVoice(manager, Integer.parseInt(value));
			break;
		}
	}

}
