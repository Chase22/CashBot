package org.chase.telegram.cashbot.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.telegram.telegrambots.logging.BotLogger;

@Entity
@Table(name = "ACCOUNT")
public class Account implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3757883082935307487L;
	@Id
	private long groupID;
	@Id
	private int userID;

	private int balance;
	
	protected Account() {};
	
	public static Account create(EntityManager manager, int userID, long groupID) {
		Account account = new Account();
		
		account.userID = userID;
		account.groupID = groupID;
		
		try {
		manager.getTransaction().begin();
		manager.persist(account);
		manager.getTransaction().commit();
		
		return account;
		} catch (Exception e) {
			BotLogger.error(Account.class.getSimpleName(), e);
			manager.getTransaction().rollback();
			return null;
		}
	}

	public static Account load(EntityManager manager, long userID, long groupID) {
		CriteriaBuilder cb = manager.getCriteriaBuilder();
		CriteriaQuery<Account> query = cb.createQuery(Account.class);

		Root<Account> root = query.from(Account.class);

		List<Predicate> predicates = new ArrayList<>();

		predicates.add(cb.equal(root.get("groupID"), groupID)); //$NON-NLS-1$
		predicates.add(cb.equal(root.get("userID"), userID)); //$NON-NLS-1$

		Predicate where = cb.and(predicates.toArray(new Predicate[predicates.size()]));

		query = query.select(root).where(where);

		TypedQuery<Account> result = manager.createQuery(query);

		Account account;
		if (result.getResultList().isEmpty()) {
			account = null;
		} else {
			account = manager.createQuery(query).getSingleResult();
		}
		return account;
	}

	public static boolean exists(EntityManager manager, long userID, long groupID) {
		return load(manager, userID, groupID) != null;
	}

	/**
	 * @return the groupID
	 */
	public long getGroupID() {
		return groupID;
	}

	/**
	 * @return the userID
	 */
	public int getUserID() {
		return userID;
	}

	/**
	 * @return the balance
	 */
	public int getBalance() {
		return balance;
	}

	/**
	 * @param balance
	 *            the balance to set
	 */
	public void setBalance(EntityManager manager, int balance) {
		manager.getTransaction().begin();
		this.balance = balance;
		manager.getTransaction().commit();
	}
	
	/**
	 * @param amount the amount to change the balance
	 */
	public void modifyBalance(EntityManager manager, int amount) {
		manager.getTransaction().begin();
		this.balance = this.balance+amount;
		manager.getTransaction().commit();
	}

	public CashUser getUser() {
		return CashUser.load(EntityUtility.getEntityManager(), userID);
	}

}
