package org.chase.telegram.cashbot.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.telegram.telegrambots.logging.BotLogger;

@Entity
@Table(name = "USER")
public class CashUser {

	@Id
	private int UserID;
	private long ChatID;
	private String Username;
	private String FirstName;
	private String LastName;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "userID")
	private List<Account> accounts = new ArrayList<>();

	public CashUser() {
	};

	/**
	 * @param userID
	 * @param chatID
	 * @param username
	 * @param firstName
	 * @param lastName
	 * @param accounts
	 */
	private CashUser(int userID, String username) {
		super();
		UserID = userID;
		Username = username;
	}

	/**
	 * @param userID
	 * @param chatID
	 * @param username
	 * @param firstName
	 * @param lastName
	 * @param accounts
	 */
	private CashUser(int userID, long chatID, String username) {
		super();
		UserID = userID;
		ChatID = chatID;
		Username = username;
	}

	/**
	 * @param userID
	 * @param username
	 * @param firstName
	 * @param lastName
	 * @param accounts
	 */
	private CashUser(int userID, String username, String firstName, String lastName) {
		super();
		UserID = userID;
		Username = username;
		FirstName = firstName;
		LastName = lastName;
	}

	/**
	 * @param userID
	 * @param chatID
	 * @param username
	 * @param firstName
	 * @param lastName
	 * @param accounts
	 */
	private CashUser(int userID, long chatID, String username, String firstName, String lastName) {
		super();
		UserID = userID;
		ChatID = chatID;
		Username = username;
		FirstName = firstName;
		LastName = lastName;
	}

	public CashUser create(EntityManager manager, int id, String username) {
		CashUser user = new CashUser(id, username);

		try {
			manager.getTransaction().begin();
			manager.persist(user);
			manager.getTransaction().commit();

			return user;
		} catch (Exception e) {
			BotLogger.error(CashUser.class.getSimpleName(), e);
			manager.getTransaction().rollback();
			return null;
		}
	}

	public static CashUser create(EntityManager manager, Integer userID, String username, String firstName,
			String lastName) {
		CashUser user = new CashUser(userID, username, firstName, lastName);

		try {
			manager.getTransaction().begin();
			manager.persist(user);
			manager.getTransaction().commit();

			return user;
		} catch (Exception e) {
			BotLogger.error(CashUser.class.getSimpleName(), e);
			manager.getTransaction().rollback();
			return null;
		}
	}

	public static CashUser create(EntityManager manager, Integer userID, Long chatID, String username, String firstName,
			String lastName) {
		CashUser user = new CashUser(userID, chatID, username, firstName, lastName);

		try {
			manager.getTransaction().begin();
			manager.persist(user);
			manager.getTransaction().commit();

			return user;
		} catch (Exception e) {
			BotLogger.error(CashUser.class.getSimpleName(), e);
			manager.getTransaction().rollback();
			return null;
		}
	}

	public static CashUser load(EntityManager manager, int ID) {
		CashUser user = (CashUser) manager.find(CashUser.class, ID);
		return user;
	}

	public static List<CashUser> findByUsername(EntityManager manager, String username) {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<CashUser> query = cb.createQuery(CashUser.class);

		Root<CashUser> root = query.from(CashUser.class);

		Predicate where = cb.equal(root.get("Username"), username); //$NON-NLS-1$

		query = query.select(root).where(where);

		TypedQuery<CashUser> result = manager.createQuery(query);

		return result.getResultList();
	}
	
	public static int getIdByUsername(EntityManager manager, String username) {
		List<CashUser> result = findByUsername(manager, username);
		
		if (result.isEmpty()) return -1;
		return findByUsername(manager, username).get(0).getID();
	}

	public static boolean exists(EntityManager manager, int ID) {
		return load(manager, ID) != null;
	}

	public void update(EntityManager manager, String username, String firstName, String lastName) {
		if (username != getUsername()) {
			this.setUsername(manager, username);
		}
		
		if (firstName != getFirstName()) {
			this.setFirstName(manager, firstName);
		}
		
		if (lastName != getLastName()) {
			this.setLastName(manager, lastName);
		}
	}

	/**
	 * @return the iD
	 */
	public int getID() {
		return UserID;
	}

	/**
	 * @return the chatID
	 */
	public long getChatID() {
		return ChatID;
	}

	/**
	 * @param chatID
	 *            the chatID to set
	 */
	public void setChatID(EntityManager manager, long chatID) {
		manager.getTransaction().begin();
		ChatID = chatID;
		manager.getTransaction().commit();
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return FirstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(EntityManager manager, String firstName) {
		manager.getTransaction().begin();
		FirstName = firstName;
		manager.getTransaction().commit();
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return LastName;
	}
	
	/**
	 * 
	 * @return the full name
	 */
	public String getName() {
		if (this.getLastName() != null) {
			return getFirstName() + " " + getLastName();
		} else {
			return getFirstName();
		}
	}
	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(EntityManager manager, String lastName) {
		manager.getTransaction().begin();
		LastName = lastName;
		manager.getTransaction().commit();
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return Username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(EntityManager manager, String username) {
		manager.getTransaction().begin();
		Username = username;
		manager.getTransaction().commit();
	}
	
	/**
	 * @return the accounts
	 */
	public List<Account> getAccounts() {
		return accounts;
	}

}
