package com.restaurant.booking.restaurant.service.controller;

import com.restaurant.booking.restaurant.model.Course;
import com.restaurant.booking.restaurant.model.CourseCreationRequest;
import com.restaurant.booking.restaurant.model.CourseType;
import com.restaurant.booking.restaurant.service.service.CourseService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseControllerImpl courseController;

    @Test
    void createCourse() {
        CourseCreationRequest courseCreationRequest = CourseCreationRequest.builder().build();

        courseController.createCourse(courseCreationRequest);
        Mockito.verify(courseService).create(courseCreationRequest);
    }

    @Test
    void getCoursesByRestaurant() {
        courseController.getCoursesByRestaurant("1234", CourseType.MAIN);
        Mockito.verify(courseService).findByRestaurant("1234", CourseType.MAIN);
    }

    @Test
    void saveImage() {
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        Course course = new Course();

        Mockito.when(courseService.findCourse("1234")).thenReturn(course);

        courseController.saveImage("1234", multipartFile);
        Mockito.verify(courseService).saveCourseImage(course, multipartFile);
    }

    @Test
    void deleteCourse() {
        courseController.deleteCourse("1234");
        Mockito.verify(courseService).delete("1234");
    }

    @Test
    void getCourseTypes() {
        ResponseEntity<List<CourseType>> courseTypes = courseController.getCourseTypes();
        Assertions.assertIterableEquals(Arrays.asList(CourseType.values()), courseTypes.getBody());
    }
}