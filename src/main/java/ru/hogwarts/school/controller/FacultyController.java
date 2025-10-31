package ru.hogwarts.school.controller;


import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение данных о факультете по id")
    public ResponseEntity<Faculty> getFacultyInfo(@PathVariable Long id) {
        Faculty faculty = facultyService.findFaculty(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @PostMapping
    @Operation(summary = "Добавление факультета")

    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.addFaculty(faculty);
    }

    @PutMapping
    @Operation(summary = "Редактирование факультета")

    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty foundFaculty = facultyService.editFaculty(faculty);
        if (foundFaculty == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(foundFaculty);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление факультета по id")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "Поиск факультета по цвету и/или названию")

    public ResponseEntity<Collection<Faculty>> findFaculties(@RequestParam(required = false) String color, @RequestParam(required = false) String name) {
        if ((color != null && !color.isBlank()) || (name != null && !name.isBlank())) {
            return ResponseEntity.ok(facultyService.findByColorOrName(color, name));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("students/{facultyId}")

    @Operation(summary = "Получение списка учеников по id факультета")

    public ResponseEntity<Collection<Student>> getAllStudentsOfFacultyById(@PathVariable Long facultyId) {
        return ResponseEntity.ok(facultyService.findFaculty(facultyId).getStudents());
    }
}