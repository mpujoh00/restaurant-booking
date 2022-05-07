package com.restaurant.booking.user.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequest {

    @NotEmpty
    @Size(min = 10)
    private String email;
    @NotEmpty
    @Size(min = 8)
    private String password;
    @NotEmpty
    private String fullname;
    @NotNull
    // TODO grupo para validar según el controlador
    private RoleName role;
}
