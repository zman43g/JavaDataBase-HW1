package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class StudentControllerTests {
    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() throws Exception {
        Assertions.assertThat(studentController).isNotNull();
    }

    @Test
    public void testGetStudentInfoProvidesCorrectInfo() throws Exception {
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/1", Student.class))
                .hasFieldOrPropertyWithValue("age", 18)
                .hasFieldOrPropertyWithValue("name", "Victor Crum");
    }

    @Test
    public void testCreateStudent() throws Exception {
        Student student = new Student();
        student.setAge(77);
        student.setName("Seven");

        ResponseEntity<Student> studentResponse = restTemplate.postForEntity("http://localhost:" + port + "/student", student, Student.class);
        Student newStudent = studentResponse.getBody();

        ResponseEntity<Student> newStudentResponse = restTemplate.getForEntity("http://localhost:" + port + "/student/" + newStudent.getId(), Student.class);
        Student student2 = newStudentResponse.getBody();

        Assertions
                .assertThat(student2)
                .hasFieldOrPropertyWithValue("age", 77)
                .hasFieldOrPropertyWithValue("name", "Seven");

        restTemplate.delete("http://localhost:" + port + "/student/" + student2.getId()); //чистим базу
    }

    @Test
    public void testEditStudent() throws Exception {
        Student student = new Student();
        student.setAge(51);
        student.setName("Severus");

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/student",
                HttpMethod.PUT,
                new HttpEntity<>(student),
                Void.class
        );
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testDeleteStudent() throws Exception {
        Student studentForDelete = new Student();
        studentForDelete.setAge(777);
        studentForDelete.setName("deleteStudentName");
        ResponseEntity<Student> studentResponse = restTemplate.postForEntity("http://localhost:" + port + "/student", studentForDelete, Student.class);
        assertThat(studentResponse.getStatusCode().is2xxSuccessful()).isTrue();

        Student newStudent = studentResponse.getBody();
        restTemplate.delete("http://localhost:" + port + "/student/" + newStudent.getId());

        String url = "/faculty?age=777";
        ResponseEntity<Student[]> response = restTemplate.getForEntity(url, Student[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Student[] students = response.getBody();
        assertThat(students.length).isEqualTo(0);
    }

    @Test
    public void testFindStudentByAge() throws Exception {
        Student studentForFind = new Student();
        int age = 199;
        studentForFind.setAge(age);
        String name = "testStudentName";

        studentForFind.setName(name);

        ResponseEntity<Student> studentResponse = restTemplate.postForEntity("http://localhost:" + port + "/student", studentForFind, Student.class);
        assertThat(studentResponse.getStatusCode().is2xxSuccessful()).isTrue();

        String url = "/student?age=" + age;
        ResponseEntity<Student[]> response = restTemplate.getForEntity(url, Student[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Student[] students = response.getBody();

        assertThat(students).hasSize(1);
        assertThat(students[0].getName()).isEqualTo(name);

        restTemplate.delete("http://localhost:" + port + "/student/" + students[0].getId()); //чистим базу

    }

    @ParameterizedTest
    @ValueSource(ints = {40, 50, 100})
    public void testFindStudentByAgeBetween(int age) throws Exception {
        int minAge = age - 1;
        int maxAge = age + 1;
        Student studentForFind = new Student();
        studentForFind.setAge(age);
        String name = "testStudentName";
        studentForFind.setName(name);

        ResponseEntity<Student> studentResponse = restTemplate.postForEntity("http://localhost:" + port + "/student", studentForFind, Student.class);
        assertThat(studentResponse.getStatusCode().is2xxSuccessful()).isTrue();

        String url = "/student/ageBetween?minAge=" + minAge + "&maxAge=" + maxAge;
        ResponseEntity<Student[]> response = restTemplate.getForEntity(url, Student[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Student[] students = response.getBody();

        assertThat(students[0].getName()).isNotBlank();
        restTemplate.delete("http://localhost:" + port + "/student/" + Optional.of(students[0].getId())); //чистим базу
    }

    @Test
    public void testGetFacultyByStudentId() throws Exception {
        String url = "/student/faculty/1";
        ResponseEntity<Faculty> response = restTemplate.getForEntity(url, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Faculty faculty = response.getBody();
        assertThat(faculty).isNotNull().hasFieldOrPropertyWithValue("name","Hugglepuff");
    }

}
