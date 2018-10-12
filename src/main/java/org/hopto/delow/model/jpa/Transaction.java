package org.hopto.delow.model.jpa;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Transaction {

    @Id
    @GeneratedValue(generator = "transactionIdSequence")
    @SequenceGenerator(name = "transactionIdSequence", initialValue = 50)
    private Integer id;

//    @Column
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private Account fromAccount;

//    @Column(nullable = false)
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Account toAccount;

//    @Column
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private Card fromCard;

//    @Column
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private Card toCard;

    @Column(nullable = false)
    private int sum;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionState transactionState;

    @Column(nullable = false)
    private LocalDateTime startedTime;

    @Column
    private LocalDateTime finishedTime;

}
