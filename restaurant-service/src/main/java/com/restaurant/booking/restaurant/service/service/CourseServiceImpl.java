package com.restaurant.booking.restaurant.service.service;

import com.restaurant.booking.restaurant.model.Course;
import com.restaurant.booking.restaurant.model.CourseCreationRequest;
import com.restaurant.booking.restaurant.model.CourseType;
import com.restaurant.booking.restaurant.service.exception.CourseNotFoundException;
import com.restaurant.booking.restaurant.service.exception.InvalidImageException;
import com.restaurant.booking.restaurant.service.exception.InvalidImageTypeException;
import com.restaurant.booking.restaurant.service.repository.CourseRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Course findCourse(String courseId) {

        log.info("Getting course with id {}", courseId);
        return courseRepository.findById(courseId).orElseThrow(() -> new CourseNotFoundException(courseId));
    }

    @Override
    public List<Course> findByRestaurant(String restaurantId, CourseType courseType) {
        log.info("Finding courses {} for restaurant {}", courseType.name(), restaurantId);
        return courseRepository.findByRestaurantIdAndCourseType(restaurantId, courseType);
    }

    @Override
    public Course create(CourseCreationRequest courseCreationRequest) {
        log.info("Creating a new course {}", courseCreationRequest.getName());
        Course course = new Course(courseCreationRequest);
        return courseRepository.save(course);
    }

    @Override
    public Course saveCourseImage(Course course, MultipartFile logo) {

        log.info("Saving logo of course {}", course.getId());

        if(!List.of("jpg", "png", "jpeg").contains(FilenameUtils.getExtension(logo.getOriginalFilename()))) {
            throw new InvalidImageTypeException(logo.getOriginalFilename());
        }
        try {
            course.setImage(logo.getBytes());
        }
        catch (IOException exception){
            throw new InvalidImageException(logo.getOriginalFilename());
        }
        return courseRepository.save(course);
    }

    @Override
    public void delete(String courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        courseOptional.ifPresent(courseRepository::delete);
    }
}
