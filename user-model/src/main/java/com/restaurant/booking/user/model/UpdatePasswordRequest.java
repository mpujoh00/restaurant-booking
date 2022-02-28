package com.restaurant.booking.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordRequest {

    @NotEmpty
    @Size(min = 10)
    private String email;
    @NotEmpty
    @Size(min = 8)
    private String currentPassword;
    @NotEmpty
    @Size(min = 8)
    private String newPassword;
}
