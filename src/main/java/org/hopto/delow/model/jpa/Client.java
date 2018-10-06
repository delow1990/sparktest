package org.hopto.delow.model.jpa;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Client {

    @Id
    @GeneratedValue
    private String id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String middleName;

    @Column
    private LocalDate birthday;

    @Column
    private String phoneNumber;

    @Column
    @Email
    private String email;

    @OneToMany(mappedBy = "client")
    private List<Card> cards;

}
