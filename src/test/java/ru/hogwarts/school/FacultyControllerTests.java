package ru.hogwarts.school;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;


import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTests {
    @LocalServerPort
    private int port;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() throws Exception {
        assertThat(facultyController).isNotNull();
    }

    @Test
    public void testCreateFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setColor("cyan");
        faculty.setName("Cyanist");

        ResponseEntity<Faculty> facultyResponse = restTemplate.postForEntity("http://localhost:" + port + "/faculty", faculty, Faculty.class);
        Faculty newFaculty = facultyResponse.getBody();

        ResponseEntity<Faculty> newFacultyResponse = restTemplate.getForEntity("http://localhost:" + port + "/faculty/" + newFaculty.getId(), Faculty.class);
        Faculty faculty2 = newFacultyResponse.getBody();

        assertThat(faculty2)
                .hasFieldOrPropertyWithValue("name", faculty.getName())
                .hasFieldOrPropertyWithValue("color", faculty.getColor())
                .hasFieldOrPropertyWithValue("id", newFaculty.getId());

        restTemplate.delete("http://localhost:" + port + "/faculty/" + faculty2.getId()); //чистим базу
    }

    @Test
    public void testEditFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setColor("magenta");
        faculty.setName("Magnetic");

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty",
                HttpMethod.PUT,
                new HttpEntity<>(faculty),
                Void.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGetFacultyInfoProvidesCorrectInfo() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty/8", Faculty.class))
                .hasFieldOrPropertyWithValue("color", "cyan")
                .hasFieldOrPropertyWithValue("name", "Cyanist");
    }

    @Test
    public void testFindFacultyByName() {
// Given - создаем данные в реальной БД
        String name = "testName";
        String color = "testColor";

        Faculty faculty = new Faculty();
        faculty.setName(name);
        faculty.setColor(color);
        Faculty savedFaculty = facultyRepository.save(faculty);

// When
        String url = "/faculty?name=" + name;
        ResponseEntity<Faculty[]> response = restTemplate.getForEntity(url, Faculty[].class);

// Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Faculty[] faculties = response.getBody();

        assertThat(faculties).hasSize(1);
        assertThat(faculties[0].getId()).isEqualTo(savedFaculty.getId());
        assertThat(faculties[0].getName()).isEqualTo(name);
        assertThat(faculties[0].getColor()).isEqualTo(color);

        restTemplate.delete("http://localhost:" + port + "/faculty/" + faculties[0].getId()); //чистим базу
    }

    @Test
    public void testGetAllStudentsOfFacultyById() throws Exception {
        Student student1 = new Student();
        student1.setName("testStudent1");
        student1.setAge(44);
        student1.setId(44L);
        Faculty facultyForTest = new Faculty();
        facultyForTest.setColor("testFacultyColor2");
        facultyForTest.setName("testFacultyName2");

        ResponseEntity<Faculty> facultyResponse = restTemplate.postForEntity("http://localhost:" + port + "/faculty", facultyForTest, Faculty.class);
        assertThat(facultyResponse.getStatusCode().is2xxSuccessful()).isTrue();
        Faculty newFaculty = facultyResponse.getBody();
        student1.setFaculty(newFaculty);

        assertThat(restTemplate.getForObject("http://localhost:" + port + "/faculty/students/" + newFaculty.getId(), Collection.class)).isNotNull();
        restTemplate.delete("http://localhost:" + port + "/faculty/" + newFaculty.getId()); //чистим базу
    }

    @Test
    public void testDeleteFaculty() throws Exception {
        Faculty facultyForDelete = new Faculty();
        facultyForDelete.setColor("deleteFacultyColor");
        facultyForDelete.setName("deleteFacultyName");
        ResponseEntity<Faculty> facultyResponse = restTemplate.postForEntity("http://localhost:" + port + "/faculty", facultyForDelete, Faculty.class);
        assertThat(facultyResponse.getStatusCode().is2xxSuccessful()).isTrue();
        Faculty newFaculty = facultyResponse.getBody();
        restTemplate.delete("http://localhost:" + port + "/faculty/" + newFaculty.getId());

        String url = "/faculty?name=deleteFacultyName";
        ResponseEntity<Faculty[]> response = restTemplate.getForEntity(url, Faculty[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Faculty[] faculties = response.getBody();
        assertThat(faculties.length).isEqualTo(0);
    }


}
