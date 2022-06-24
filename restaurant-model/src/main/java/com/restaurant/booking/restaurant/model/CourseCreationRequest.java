package com.restaurant.booking.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CourseCreationRequest {

    @NotEmpty
    private String restaurantId;
    @NotEmpty
    private String name;
    @NotEmpty
    private String ingredients;
    @NotEmpty
    private CourseType courseType;
}
