package com.restaurant.booking.restaurant.service.repository;

import com.restaurant.booking.restaurant.model.Course;
import com.restaurant.booking.restaurant.model.CourseType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CourseRepository extends MongoRepository<Course, String> {

    List<Course> findByRestaurantIdAndCourseType(String restaurantId, CourseType courseType);

}
