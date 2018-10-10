package org.hopto.delow.dao;

import org.hopto.delow.model.jpa.Card;

import javax.persistence.EntityManager;

public class CardDao implements DefaultDao<Card> {

    @Override
    public Card createEntity(EntityManager entityManager, Card card) {
        return entityManager.merge(card);
    }

    @Override
    public Card findById(EntityManager entityManager, String id) {
        return entityManager.find(Card.class, Integer.valueOf(id));
    }

    @Override
    public Card updateEntity(EntityManager entityManager, Card card) {
        return entityManager.merge(card);
    }

    @Override
    public void deleteById(EntityManager entityManager, String id) {
        entityManager.createQuery("delete from Card where id = :id")
                .setParameter("id", Integer.valueOf(id))
                .executeUpdate();
    }
}
