package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.entity.StudentsForSQLResponse;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@RestController

public class StudentsSQLController {
    private final StudentService studentService;

    public StudentsSQLController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students/getAllStudents")
    @Operation(summary = "Получение количества учеников")
    public short getAllStudentsOfHogwarts() {
        return studentService.getQuantityOfStudentsOfHogwarts();
    }

    @GetMapping("/students/averageAge")
    @Operation(summary = "Получение среднего возраста учеников")
    public float getAverageAgeOfStudents() {
        return studentService.getAverageAgeOfStudentsOfHogwarts();
    }

    @GetMapping("/students/getFiveWithHighId")
    @Operation(summary = "Получение пяти учеников с наибольшими id")
    public List<StudentsForSQLResponse> getFiveWithHighId() {
        return studentService.getFiveStudentsWithHighId();
    }

}
