package org.hopto.delow.model.jpa;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Card {

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String number;

    @Column
    private String cardHolder;

    @Column
    private String expDate;

    @Column
    private String codeHash;

    @Column
    @Enumerated(EnumType.STRING)
    private CardType cardType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

}
