package ru.hogwarts.school.service;

import java.util.Collection;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.entity.StudentsForSQLResponse;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

@Service
public class StudentService {
    Logger logger = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        logger.info("Was invoked addStudent method for Student {}", student);
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        logger.info("Was invoked findStudent method for id {}", id);
        return studentRepository.findById(id).get();
    }

    public Student editStudent(Student student) {
        logger.info("Was invoked editStudent method for Student {}", student);
        return studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        logger.info("Was invoked deleteStudent method with id {}", id);
        studentRepository.deleteById(id);
    }

    public Collection<Student> findByAge(int age) {
        logger.info("Was invoked findByAge method with age {}", age);
        return studentRepository.findByAge(age);
    }

    public Collection<Student> findByAgeBetween(int minAge, int maxAge) {
        logger.info("Was invoked findByAgeBetween method with minAge {}, and maxAge {}", minAge,maxAge);
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public Faculty findFacultyByStudentId(long id) {
        logger.info("Was invoked findFacultyByStudentId method with id {}", id);
        return findStudent(id).getFaculty();
    }

    public short getQuantityOfStudentsOfHogwarts() {
        logger.info("Was invoked getQuantityOfStudentsOfHogwarts method");
        return studentRepository.getQuantityOfStudents();
    }

    public float getAverageAgeOfStudentsOfHogwarts() {
        logger.info("Was invoked  getAverageAgeOfStudentsOfHogwarts method");
        return studentRepository.getAverageAgeOfStudents();
    }

    public List<StudentsForSQLResponse> getFiveStudentsWithHighId() {
        logger.info("Was invoked  getFiveStudentsWithHighId method");
        return studentRepository.getFiveStudentsWithHighId();
    }
}