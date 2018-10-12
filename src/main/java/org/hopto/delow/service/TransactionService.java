package org.hopto.delow.service;

import org.hopto.delow.dao.*;
import org.hopto.delow.model.jpa.Account;
import org.hopto.delow.model.jpa.Card;
import org.hopto.delow.model.jpa.Transaction;
import org.hopto.delow.model.jpa.TransactionState;
import org.hopto.delow.service.exception.InvalidCardDataException;
import org.hopto.delow.service.exception.NoSuchCardException;
import org.hopto.delow.service.exception.NotEnoughMoneyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    private final AccountDao accountDao;

    private final CardDao cardDao;

    private final TransactionDao transactionDao;

    private ThreadLocal<EntityManager> em;

    public TransactionService(AccountDao accountDao, CardDao cardDao, TransactionDao transactionDao, EntityManagerFactory entityManagerFactory) {
        this.accountDao = accountDao;
        this.cardDao = cardDao;
        this.transactionDao = transactionDao;
        em = ThreadLocal.withInitial(entityManagerFactory::createEntityManager);
    }

    public Transaction createTransaction(String fromCardNumber, String fromCardCardHolder, LocalDate fromCardExpDate, String fromCardKey, String toCardNumber, Integer sum) throws NotEnoughMoneyException, InvalidCardDataException, NoSuchCardException {
        EntityManager entityManager = em.get();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Transaction moneyTransaction = new Transaction();
        moneyTransaction.setStartedTime(LocalDateTime.now());

        Card fromCard;
        try {
             fromCard = cardDao.findByNumberJoinAccount(entityManager, fromCardNumber);
        } catch (NoResultException e) {
            transaction.rollback();
            throw new NoSuchCardException();
        }


        logger.debug("from card cardHolder: request: {}, DB: {}", fromCardCardHolder, fromCard.getCardHolder());
        logger.debug("from card expDate: request: {}, DB: {}", fromCardExpDate, fromCard.getExpDate());
        logger.debug("from card codeHash: request: {}, DB: {}", fromCardKey, fromCard.getCodeHash());

        if (!fromCard.getCardHolder().equals(fromCardCardHolder) || !fromCard.getExpDate().equals(fromCardExpDate) || !fromCard.getCodeHash().equals(fromCardKey)) {
            transaction.rollback();
            throw new InvalidCardDataException();
        }

        Card toCard;
        try {
            toCard = cardDao.findByNumberJoinAccount(entityManager, toCardNumber);
        } catch (NoResultException e) {
            transaction.rollback();
            throw new NoSuchCardException();
        }

        Account fromAccount = fromCard.getAccount();
        long newAmount = fromAccount.getCurrencyAmount() - sum;
        if (newAmount < 0) {
            transaction.rollback();
            throw new NotEnoughMoneyException();
        }
        fromAccount.setCurrencyAmount(newAmount);
        fromAccount.setAuthorizedCurrencyAmount(newAmount);

        Account toAccount = toCard.getAccount();
        toAccount.setCurrencyAmount(toAccount.getCurrencyAmount() + sum);
        toAccount.setAuthorizedCurrencyAmount(toAccount.getAuthorizedCurrencyAmount() + sum);

        moneyTransaction.setFromAccount(fromAccount);
        moneyTransaction.setToAccount(toAccount);
        moneyTransaction.setFromCard(fromCard);
        moneyTransaction.setToCard(toCard);
        moneyTransaction.setSum(sum);
        moneyTransaction.setTransactionState(TransactionState.COMPLETED);

        accountDao.updateEntity(entityManager, fromAccount);
        accountDao.updateEntity(entityManager, toAccount);

        moneyTransaction.setFinishedTime(LocalDateTime.now());
        moneyTransaction = transactionDao.createEntity(entityManager, moneyTransaction);
        transaction.commit();

        logger.info("Created money transaction: {}; fromCard: {}, toCard: {}", moneyTransaction, fromCard.getNumber(), toCard.getNumber());
        return moneyTransaction;
    }

    public Transaction createTransaction(String toCardNumber, Integer sum) {
        EntityManager entityManager = em.get();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Transaction moneyTransaction = new Transaction();
        moneyTransaction.setStartedTime(LocalDateTime.now());

        Card toCard = cardDao.findByNumberJoinAccount(entityManager, toCardNumber);
        Account toAccount = toCard.getAccount();
        toAccount.setCurrencyAmount(toAccount.getCurrencyAmount() + sum);
        toAccount.setAuthorizedCurrencyAmount(toAccount.getAuthorizedCurrencyAmount() + sum);

        moneyTransaction.setToAccount(toAccount);
        moneyTransaction.setToCard(toCard);
        moneyTransaction.setSum(sum);
        moneyTransaction.setTransactionState(TransactionState.COMPLETED);

        accountDao.updateEntity(entityManager, toAccount);

        moneyTransaction.setFinishedTime(LocalDateTime.now());
        moneyTransaction = transactionDao.createEntity(entityManager, moneyTransaction);

        transaction.commit();
        logger.info("Created money transaction: {}; toCard: {}", moneyTransaction, toCard.getNumber());
        return null;
    }

}
