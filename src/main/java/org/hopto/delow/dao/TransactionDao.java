package org.hopto.delow.dao;

import org.hopto.delow.model.jpa.Transaction;

import javax.persistence.EntityManager;

public class TransactionDao implements DefaultDao<Transaction> {

    @Override
    public Transaction createEntity(EntityManager entityManager, Transaction transaction) {
        return entityManager.merge(transaction);
    }

    @Override
    public Transaction findById(EntityManager entityManager, String transactionId) {
        return entityManager.find(Transaction.class, Integer.valueOf(transactionId));
    }

    @Override
    public Transaction updateEntity(EntityManager entityManager, Transaction transaction) {
        return entityManager.merge(transaction);
    }

    @Override
    public void deleteById(EntityManager entityManager, String transactionId) {
        entityManager.createQuery("delete from Transaction where id = :id")
                .setParameter("id", Integer.valueOf(transactionId))
                .executeUpdate();
    }
}
