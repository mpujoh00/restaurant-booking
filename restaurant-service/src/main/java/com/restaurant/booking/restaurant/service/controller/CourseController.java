package com.restaurant.booking.restaurant.service.controller;

import com.restaurant.booking.restaurant.model.Course;
import com.restaurant.booking.restaurant.model.CourseCreationRequest;
import com.restaurant.booking.restaurant.model.CourseType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Course")
@RequestMapping("/api/v1/courses")
public interface CourseController {

    @PreAuthorize("hasAuthority('ROLE_RESTAURANT')")
    @Operation(description = "Creates the course", operationId = "createCourse")
    @PostMapping
    ResponseEntity<Course> createCourse(@RequestBody @Valid CourseCreationRequest courseCreationRequest);

    @PreAuthorize("hasAuthority('ROLE_RESTAURANT') or hasAuthority('ROLE_CLIENT')")
    @Operation(description = "Gets all courses", operationId = "getCoursesByRestaurant")
    @GetMapping
    ResponseEntity<List<Course>> getCoursesByRestaurant(@RequestParam("restaurantId") String restaurantId,
                                                        @RequestParam("courseType") CourseType courseType);

    @PreAuthorize("hasAuthority('ROLE_RESTAURANT')")
    @Operation(description = "Updates course's image", operationId = "saveImage")
    @PutMapping("/{courseId}/image")
    ResponseEntity<Course> saveImage(@PathVariable String courseId, @RequestParam("file") MultipartFile image);

    @PreAuthorize("hasAuthority('ROLE_RESTAURANT')")
    @Operation(description = "Deletes the course", operationId = "deleteCourse")
    @DeleteMapping("/{courseId}")
    ResponseEntity<Void> deleteCourse(@PathVariable String courseId);

    @Operation(description = "Gets all course types", operationId = "getCourseTypes")
    @GetMapping("/types")
    ResponseEntity<List<CourseType>> getCourseTypes();
}
