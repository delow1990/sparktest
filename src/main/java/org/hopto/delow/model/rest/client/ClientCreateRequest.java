package org.hopto.delow.model.rest.client;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class ClientCreateRequest {

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    private String middleName;

    @NotNull
    private LocalDate birthday;

    @NotEmpty
    private String phoneNumber;

    @NotEmpty
    @Email
    private String email;

}
