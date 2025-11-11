package ru.hogwarts.school.controller;


import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{id}")

    @Operation(summary = "Получение данных ученика по id ")
    public ResponseEntity<Student> getStudentInfo(@PathVariable Long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PostMapping

    @Operation(summary = "Добавление ученика")

    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        return ResponseEntity.ok(studentService.addStudent(student));
    }

    @PutMapping

    @Operation(summary = "Редактирование данных ученика")

    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student foundStudent = studentService.editStudent(student);
        if (foundStudent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundStudent);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление ученика по id")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping

    @Operation(summary = "Поиск ученика по возрасту")

    public ResponseEntity<Collection<Student>> findStudents(@RequestParam(required = false) int age) {
        if (age > 0) {
            return ResponseEntity.ok(studentService.findByAge(age));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping(path = "/ageBetween")

    @Operation(summary = "Получение ученика в возрастном интервале")

    public ResponseEntity<Collection<Student>> findStudentsByAgeBetween(@RequestParam(required = false) int minAge, @RequestParam(required = false) int maxAge) {
        if (minAge > 0 & maxAge > minAge) {
            return ResponseEntity.ok(studentService.findByAgeBetween(minAge, maxAge));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("faculty/{studentId}")

    @Operation(summary = "Получение факультета по id ученика")

    public ResponseEntity<Faculty> getFacultyByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(studentService.findFacultyByStudentId(studentId));
    }


}
