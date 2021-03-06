package com.restaurant.booking.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseCreationRequest {

    @NotEmpty
    private String restaurantId;
    @NotEmpty
    private String name;
    @NotEmpty
    private String ingredients;
    @NotNull
    private Double price;
    @NotNull
    private CourseType courseType;
}
