package com.restaurant.booking.booking.model;

import com.restaurant.booking.table.model.Table;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
public class ReservSlotsCreationRequest {

    @NotEmpty
    private String restaurantId;
    @NotNull
    private List<LocalTime> reservationHours;
    private Table table;
}
