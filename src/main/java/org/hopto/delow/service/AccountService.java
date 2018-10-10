package org.hopto.delow.service;

import org.hopto.delow.dao.AccountDao;
import org.hopto.delow.dao.ClientDao;
import org.hopto.delow.dao.JpaManager;
import org.hopto.delow.model.jpa.Account;
import org.hopto.delow.model.jpa.Client;
import org.hopto.delow.model.jpa.CurrencyType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    private final AccountDao accountDao;

    private final ClientDao clientDao;

    private ThreadLocal<EntityManager> em;

    public AccountService(AccountDao accountDao, ClientDao clientDao) {
        this.accountDao = accountDao;
        this.clientDao = clientDao;
        em = ThreadLocal.withInitial(JpaManager.INSTANCE::getEntityManager);
    }

    public Account createAccount(String clientId, CurrencyType currencyType) {
        EntityManager entityManager = em.get();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Client client = clientDao.findById(entityManager, clientId);

        if (client == null)
            throw new RuntimeException("No client with id = " + clientId);

        Account account = new Account();
        account.setCurrencyType(currencyType);
        account.setCurrencyAmount(0L);
        account.setAuthorizedCurrencyAmount(0L);
        account.setBlocked(false);

        account.setClient(client);
        client.getAccounts().add(account);

        account = accountDao.createEntity(entityManager, account);
        clientDao.updateEntity(entityManager, client);

        transaction.commit();
        logger.info("Created account: {}", account);
        return account;
    }

    public void blockAccount(String accountId) {
        EntityManager entityManager = em.get();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        accountDao.blockAccount(entityManager, accountId);

        transaction.commit();
    }
}
