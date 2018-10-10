package org.hopto.delow.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.hopto.delow.dao.AccountDao;
import org.hopto.delow.dao.CardDao;
import org.hopto.delow.dao.ClientDao;
import org.hopto.delow.dao.JpaManager;
import org.hopto.delow.model.jpa.Account;
import org.hopto.delow.model.jpa.Card;
import org.hopto.delow.model.jpa.CardType;
import org.hopto.delow.model.jpa.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.concurrent.ThreadLocalRandom;

public class CardService {

    private static final Logger logger = LoggerFactory.getLogger(CardService.class);

    private final ClientDao clientDao;

    private final CardDao cardDao;

    private final AccountDao accountDao;

    private ThreadLocal<EntityManager> em;

    private static final int YEARS_TO_ADD_ON_CREATION = 3;

    public CardService(ClientDao clientDao, CardDao cardDao, AccountDao accountDao) {
        this.clientDao = clientDao;
        this.cardDao = cardDao;
        this.accountDao = accountDao;
        em = ThreadLocal.withInitial(JpaManager.INSTANCE::getEntityManager);
    }

    public Card createCard(String accountId, String cardHolder, CardType cardType) {
        EntityManager entityManager = em.get();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Account account = accountDao.findById(entityManager, accountId);

        Card card = new Card();

        card.setCardType(cardType);
        card.setCardHolder(cardHolder);

        LocalDate date = LocalDate.now()
                .plus(YEARS_TO_ADD_ON_CREATION, ChronoUnit.YEARS)
                .with(TemporalAdjusters.lastDayOfMonth());
        card.setExpDate(date);

        int code = ThreadLocalRandom.current().nextInt(100, 999);

        byte[] bytes = ByteBuffer.allocate(Integer.BYTES).putInt(code).array();

        String codeHash = DigestUtils.sha256Hex(bytes);
        card.setCodeHash(codeHash);

        card.setAccount(account);
        account.getCards().add(card);

        card = cardDao.createEntity(entityManager, card);

        // pretend we send card info somewhere where the actual card will be made
        transaction.commit();

        logger.info("Created card: {} with code: {}", card, code);
        return card;
    }

}
