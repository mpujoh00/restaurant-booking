package com.restaurant.booking.user.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PasswordChangeResponse {

    private String token;
    private String errorMessage;
}
