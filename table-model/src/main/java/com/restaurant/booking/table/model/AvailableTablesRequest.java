package com.restaurant.booking.table.model;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

public class AvailableTablesRequest {

    @NotEmpty
    private LocalDateTime date;
    @NotEmpty
    private Integer numPeople;
}
