package com.restaurant.booking.user.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequest {

    @NotEmpty
    @Size(min = 10)
    private String email;
    @Size(min = 8)
    private String password;
    private String fullname;
}
