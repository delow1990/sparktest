package org.hopto.delow.model.rest.client;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
public class ClientUpdateRequest {

    @NotEmpty
    private String id;

    private String firstName;

    private String lastName;

    private String middleName;

    private LocalDate birthday;

    private String phoneNumber;

    @Email
    private String email;

}
