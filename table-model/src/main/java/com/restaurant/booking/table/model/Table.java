package com.restaurant.booking.table.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
@Document(collection = "tables")
public class Table {

    @Id
    private String id;

    private Integer number;
    private Integer minPeople;
    private Integer maxPeople;
    private String restaurantId;

    public Table(TableCreationRequest tableCreationRequest, String restaurantId){
        this.number = tableCreationRequest.getNumber();
        this.minPeople = tableCreationRequest.getMinPeople();
        this.maxPeople = tableCreationRequest.getMaxPeople();
        this.restaurantId = restaurantId;
    }
}
