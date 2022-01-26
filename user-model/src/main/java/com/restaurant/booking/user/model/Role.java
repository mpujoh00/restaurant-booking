package com.restaurant.booking.user.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Document(collection = "roles")
public class Role {

    @Id
    private String id;
    private RoleName name;

    public Role(RoleName name) {
        this.name = name;
    }
}
