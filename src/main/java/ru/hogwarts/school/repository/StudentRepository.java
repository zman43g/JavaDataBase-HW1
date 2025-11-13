package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.entity.StudentsForSQLResponse;
import ru.hogwarts.school.model.Student;

import java.util.List;
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByAge(int age);
    List<Student> findByAgeBetween(int minAge, int maxAge);
    @Query(value = "select count(*)from student", nativeQuery = true)
    short getQuantityOfStudents();

    @Query(value = "select avg(age)from student", nativeQuery = true)
    float getAverageAgeOfStudents();

    @Query(value = "select id,name from student order by id DESC limit 5", nativeQuery = true)
    List<StudentsForSQLResponse> getFiveStudentsWithHighId();
}
