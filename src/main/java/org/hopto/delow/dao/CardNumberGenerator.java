package org.hopto.delow.dao;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.dialect.internal.StandardDialectResolver;
import org.hibernate.engine.jdbc.dialect.spi.DatabaseMetaDataDialectResolutionInfoAdapter;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolver;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;

import javax.persistence.EntityManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CardNumberGenerator {

    private static final String LEFT_NUMBER_PART = "532371";
    private final ReturningWork<Long> getNextValueFromSequence;
    private ThreadLocal<EntityManager> em = ThreadLocal.withInitial(JpaManager.INSTANCE::getEntityManager);

    private static final String SEQUENCE_NAME = "CARD_NUMBER_SEQUENCE";

    public CardNumberGenerator() {
        DialectResolver dialectResolver = StandardDialectResolver.INSTANCE;
        getNextValueFromSequence = connection -> {
            DatabaseMetaDataDialectResolutionInfoAdapter adapter = new DatabaseMetaDataDialectResolutionInfoAdapter(connection.getMetaData());
            Dialect dialect = dialectResolver.resolveDialect(adapter);
            try (PreparedStatement preparedStatement = connection.prepareStatement(dialect.getSequenceNextValString(SEQUENCE_NAME));
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return resultSet.getLong(1);
            }
        };

        boolean exists = checkIfSequenceExists();
        if (!exists)
            createSequence();
    }

    private boolean checkIfSequenceExists() {
        boolean exists = true;
        em.get().getTransaction().begin();
        try {
            em.get().unwrap(Session.class).doReturningWork(getNextValueFromSequence);
        } catch (HibernateException e) {
            exists = false;
        }
        em.get().getTransaction().rollback();
        return exists;
    }

    private void createSequence() {
        DialectResolver dialectResolver = StandardDialectResolver.INSTANCE;
        Work createSequence = connection -> {
            DatabaseMetaDataDialectResolutionInfoAdapter adapter = new DatabaseMetaDataDialectResolutionInfoAdapter(connection.getMetaData());
            Dialect dialect = dialectResolver.resolveDialect(adapter);
            String[] createSequenceStrings = dialect.getCreateSequenceStrings(SEQUENCE_NAME, 1, 1);

            for (String s : createSequenceStrings) {
                try (PreparedStatement preparedStatement = connection.prepareStatement(s)) {
                    preparedStatement.execute();
                }
            }
        };

        em.get().getTransaction().begin();
        em.get().unwrap(Session.class).doWork(createSequence);
        em.get().getTransaction().commit();
    }

    public String getNewNumber() {
        Session session = em.get().unwrap(Session.class);
        Long number = session.doReturningWork(getNextValueFromSequence);

        String rightPartNumber = StringUtils.leftPad(number.toString(), 10, '0');

        return LEFT_NUMBER_PART + rightPartNumber;
    }

}
