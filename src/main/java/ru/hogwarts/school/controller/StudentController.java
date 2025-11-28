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
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

    @GetMapping(path = "/allStudentsFirstLetterA")

    @Operation(summary = "Получение всех учеников имя которых начинается с буквы А")

    public ResponseEntity<List<Student>> getAllStudentsWithFirstLetterA() {
        return ResponseEntity.ok(studentService.findAllStudentsWithFirstLetterA());
    }

    @GetMapping(path = "/averageAge")

    @Operation(summary = "Получение среднего возраста всех учеников")

    public ResponseEntity<Double> getAverageAge() {
        return ResponseEntity.ok(studentService.averageAgeOfStudents());
    }

    @GetMapping(path = "/calculationHW")

    @Operation(summary = "Расчёт суммы чисел от единицы до миллиона ")

    public ResponseEntity<Integer> calulationForHW() {
        return ResponseEntity.ok(studentService.sumFromOneToMillion());
    }

    @GetMapping(path = "/students/print-parallel")
    @Operation(summary = "Печать имён учеников с использованием потоков ")

    public void printParallelNamesOfStudents() {
        List<Student> students = studentService.getAllStudents();
        System.out.println("Начало вывода имен студентов:");

        System.out.println("Первый ученик " + students.get(0).getName());
        System.out.println("Второй ученик " + students.get(1).getName());

        new Thread(() -> {
            System.out.println("Третий ученик " + students.get(2).getName());
            System.out.println("Четвертый ученик " + students.get(3).getName());
        }).start();

        new Thread(() -> {
            System.out.println("Пятый ученик " + students.get(4).getName());
            System.out.println("Шестой ученик " + students.get(5).getName());
        }).start();

    }


    @GetMapping(path = "/students/print-synchronized")
    @Operation(summary = "Печать имён учеников в синхронном режиме ")

    public void printSynchronizedNamesOfStudents() {
        System.out.println("Начало вывода имен студентов:");
        List<Student> students = studentService.getAllStudents();

        studentService.printStudentName("Первый ученик " + students.get(0).getName());
        studentService.printStudentName("Второй ученик " + students.get(1).getName());

        Thread firstThread = new Thread(() -> {

            studentService.printStudentName("Третий ученик " + students.get(2).getName());
            studentService.printStudentName("Четвертый ученик " + students.get(3).getName());
        });

        Thread secondThread = new Thread(() -> {
            studentService.printStudentName("Пятый ученик " + students.get(4).getName());
            studentService.printStudentName("Шестой ученик " + students.get(5).getName());
        });

        firstThread.start();
        secondThread.start();

        try {
            firstThread.join();
            secondThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


    }


}
