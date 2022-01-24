package com.restaurant.booking.user.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequest {

    @NotEmpty // can add messages
    @Size(min = 10)
    private final String email;
    @NotEmpty
    @Size(min = 8)
    private final String password;
    @NotEmpty
    private final String fullname;
    // can't use @NotEmpty with Enum
    private final Role role;
}
