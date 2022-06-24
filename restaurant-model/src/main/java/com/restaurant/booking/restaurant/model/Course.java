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
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Binary image;
    private CourseType courseType;
    private String restaurantId;

    public Course(CourseCreationRequest courseCreationRequest) {
        this.name = courseCreationRequest.getName();
        this.ingredients = courseCreationRequest.getIngredients();
        this.courseType = courseCreationRequest.getCourseType();
    }

    public byte[] getImage(){
        return this.image.getData();
    }

    public void setImage(byte[] logo){
        this.image = new Binary(BsonBinarySubType.BINARY, logo);
    }
}
