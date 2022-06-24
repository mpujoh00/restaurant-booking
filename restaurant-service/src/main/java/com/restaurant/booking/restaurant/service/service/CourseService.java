package com.restaurant.booking.restaurant.service.service;

import com.restaurant.booking.restaurant.model.Course;
import com.restaurant.booking.restaurant.model.CourseCreationRequest;
import com.restaurant.booking.restaurant.model.CourseType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CourseService {

    Course findCourse(String courseId);

    List<Course> findByRestaurant(String restaurantId, CourseType courseType);

    Course create(CourseCreationRequest courseCreationRequest);

    Course saveCourseImage(Course course, MultipartFile logo);

    void delete(String courseId);
}
