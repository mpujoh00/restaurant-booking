package com.restaurant.booking.restaurant.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Document(collection = "categories")
public class Category {

    @Id
    private String id;
    private String name;

    public Category(String name){
        this.name = name;
    }
}
