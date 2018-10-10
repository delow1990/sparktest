package org.hopto.delow.dao;

import org.hopto.delow.model.jpa.Account;

import javax.persistence.EntityManager;

public class AccountDao implements DefaultDao<Account> {

    @Override
    public Account createEntity(EntityManager entityManager, Account account) {
        return entityManager.merge(account);
    }

    @Override
    public Account findById(EntityManager entityManager, String accountId) {
        return entityManager.find(Account.class, Integer.valueOf(accountId));
    }

    @Override
    public Account updateEntity(EntityManager entityManager, Account account) {
        return entityManager.merge(account);
    }

    @Override
    public void deleteById(EntityManager entityManager, String accountId) {
        entityManager.createQuery("delete from Account where id = :id")
                .setParameter("id", Integer.valueOf(accountId))
                .executeUpdate();
    }

    public void blockAccount(EntityManager entityManager, String accountId) {
        entityManager.createQuery("update Account set blocked = true where id = :id")
                .setParameter("id", Integer.valueOf(accountId))
                .executeUpdate();
    }
}
