package com.restaurant.booking.restaurant.service.controller;

import com.restaurant.booking.restaurant.model.Course;
import com.restaurant.booking.restaurant.model.CourseCreationRequest;
import com.restaurant.booking.restaurant.model.CourseType;
import com.restaurant.booking.restaurant.service.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class CourseControllerImpl implements CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseControllerImpl(CourseService courseService) {
        this.courseService = courseService;
    }

    @Override
    public ResponseEntity<Course> createCourse(@Valid CourseCreationRequest courseCreationRequest) {
        return ResponseEntity.ok(courseService.create(courseCreationRequest));
    }

    @Override
    public ResponseEntity<List<Course>> getCoursesByRestaurant(String restaurantId, CourseType courseType) {
        return ResponseEntity.ok(courseService.findByRestaurant(restaurantId, courseType));
    }

    @Override
    public ResponseEntity<Course> saveImage(String courseId, MultipartFile image) {
        Course course = courseService.findCourse(courseId);
        return ResponseEntity.ok(courseService.saveCourseImage(course, image));
    }

    @Override
    public ResponseEntity<Void> deleteCourse(String courseId) {
        courseService.delete(courseId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<CourseType>> getCourseTypes() {
        List<CourseType> courseTypes = new ArrayList<>(Arrays.asList(CourseType.values()));
        return ResponseEntity.ok(courseTypes);
    }
}
