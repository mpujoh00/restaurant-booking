package com.restaurant.booking.restaurant.service.service;

import com.restaurant.booking.restaurant.model.Course;
import com.restaurant.booking.restaurant.model.CourseCreationRequest;
import com.restaurant.booking.restaurant.model.CourseType;
import com.restaurant.booking.restaurant.service.exception.CourseNotFoundException;
import com.restaurant.booking.restaurant.service.exception.InvalidImageException;
import com.restaurant.booking.restaurant.service.exception.InvalidImageTypeException;
import com.restaurant.booking.restaurant.service.repository.CourseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    @Test
    void findCourse() {
        Mockito.when(courseRepository.findById("C1")).thenReturn(Optional.of(Course.builder().build()));
        courseService.findCourse("C1");
        Mockito.verify(courseRepository).findById("C1");
    }

    @Test
    void findCourse_notFound() {
        Mockito.when(courseRepository.findById("C1")).thenReturn(Optional.empty());
        Assertions.assertThrows(CourseNotFoundException.class, () -> courseService.findCourse("C1"));
        Mockito.verify(courseRepository).findById("C1");
    }

    @Test
    void findByRestaurant() {
        courseService.findByRestaurant("R123", CourseType.STARTER);
        Mockito.verify(courseRepository).findByRestaurantIdAndCourseType("R123", CourseType.STARTER);
    }

    @Test
    void create() {
        CourseCreationRequest courseCreationRequest = new CourseCreationRequest("R123", "Course 1", null, 40.0, CourseType.STARTER);
        Course course = new Course(courseCreationRequest);
        courseService.create(courseCreationRequest);
        Mockito.verify(courseRepository).save(Mockito.eq(course));
    }

    @Test
    void saveCourseImage() throws IOException {
        Course course = Course.builder().id("C123").build();
        MultipartFile image = Mockito.mock(MultipartFile.class);
        byte[] logo = new byte[]{};
        course.setImage(logo);

        Mockito.when(image.getOriginalFilename()).thenReturn("image.png");
        Mockito.when(image.getBytes()).thenReturn(logo);

        courseService.saveCourseImage(course, image);

        Mockito.verify(courseRepository).save(Mockito.eq(course));
    }

    @Test
    void saveCourseImage_wrongType() {
        Course course = Course.builder().id("C123").build();
        MultipartFile image = Mockito.mock(MultipartFile.class);
        byte[] logo = new byte[]{};
        course.setImage(logo);

        Mockito.when(image.getOriginalFilename()).thenReturn("image.pdf");

        Assertions.assertThrows(InvalidImageTypeException.class,
                () -> courseService.saveCourseImage(course, image));

        Mockito.verify(courseRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void saveCourseImage_invalidImage() throws IOException {
        Course course = Course.builder().id("C123").build();
        MultipartFile image = Mockito.mock(MultipartFile.class);
        byte[] logo = new byte[]{};
        course.setImage(logo);

        Mockito.when(image.getOriginalFilename()).thenReturn("image.png");
        Mockito.when(image.getBytes()).thenThrow(IOException.class);

        Assertions.assertThrows(InvalidImageException.class,
                () -> courseService.saveCourseImage(course, image));

        Mockito.verify(courseRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    void delete() {
        Mockito.when(courseRepository.findById("C123")).thenReturn(Optional.of(Course.builder().build()));
        courseService.delete("C123");
        Mockito.verify(courseRepository).delete(Mockito.any());
    }

    @Test
    void delete_notFound() {
        Mockito.when(courseRepository.findById("C123")).thenReturn(Optional.empty());
        courseService.delete("C123");
        Mockito.verify(courseRepository, Mockito.never()).delete(Mockito.any());
    }
}