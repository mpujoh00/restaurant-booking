package com.restaurant.booking.table.model;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class TableCreationRequest {

    // se pueden especificar mensajes
    @NonNull
    private Integer number;
    @NonNull
    private Integer minPeople;
    @NonNull
    private Integer maxPeople;
}
