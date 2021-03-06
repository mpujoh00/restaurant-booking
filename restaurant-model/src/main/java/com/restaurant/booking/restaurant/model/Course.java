package com.restaurant.booking.restaurant.model;

import lombok.*;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
@Document(collection = "courses")
public class Course {

    @Id
    private String id;
    private String name;
    private String ingredients;
    private Double price;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Binary image;
    private CourseType courseType;
    private String restaurantId;

    public Course(CourseCreationRequest courseCreationRequest) {
        this.name = courseCreationRequest.getName();
        this.ingredients = courseCreationRequest.getIngredients();
        this.price = courseCreationRequest.getPrice();
        this.courseType = courseCreationRequest.getCourseType();
        this.restaurantId = courseCreationRequest.getRestaurantId();
    }

    public byte[] getImage(){
        return this.image != null ? this.image.getData() : null;
    }

    public void setImage(byte[] image){
        if(image != null)
            this.image = new Binary(BsonBinarySubType.BINARY, image);
    }
}
