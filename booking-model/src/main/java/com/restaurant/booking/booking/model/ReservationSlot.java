package com.restaurant.booking.booking.model;

import com.restaurant.booking.table.model.Table;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
@Document(collection = "reservationSlots")
public class ReservationSlot {

    @Id
    private String id;
    private LocalDate date;
    private LocalTime time;
    private ReservationSlotStatus status;
    private String restaurantId;
    @DBRef
    private Table table;
}
