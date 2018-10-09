package org.hopto.delow.model.rest.client;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate birthday;

    private String phoneNumber;

    @Email
    private String email;

}
