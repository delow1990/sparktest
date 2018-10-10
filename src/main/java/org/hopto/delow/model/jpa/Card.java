package org.hopto.delow.model.jpa;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"NUMBER", "EXPDATE"}))
public class Card {

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String number;

    @Column
    private String cardHolder;

    @Column
    private LocalDate expDate;

    @Column
    private String codeHash;

    @Column
    @Enumerated(EnumType.STRING)
    private CardType cardType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ACCOUNT_ID")
    @ToString.Exclude
    private Account account;

}
