package com.restaurant.booking.user.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class LoginRequest {

    @NotEmpty
    @Size(min = 10)
    private final String email;
    @NotEmpty
    @Size(min = 8)
    private final String password;
}
