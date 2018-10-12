package org.hopto.delow.model.jpa;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Account {

    @Id
    @GeneratedValue(generator = "accountIdSequence")
    @SequenceGenerator(name = "accountIdSequence", initialValue = 50)
    private Integer id;

    @Column(nullable = false)
    private Long currencyAmount;

    @Column(nullable = false)
    private Long authorizedCurrencyAmount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;

    @Column(nullable = false)
    private boolean blocked = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CLIENT_ID", nullable = false)
    @ToString.Exclude
    private Client client;

    @OneToMany(mappedBy = "account")
    @ToString.Exclude
    private List<Card> cards;

}
