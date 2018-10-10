package org.hopto.delow.model.jpa;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Client {

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String middleName;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private LocalDate birthday;

    @Column
    private String phoneNumber;

    @Column
    @Email
    private String email;

    @Column(nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "client")
    @JsonIgnore
    @ToString.Exclude
    private List<Account> accounts;

}
