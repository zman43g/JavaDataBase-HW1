package ru.hogwarts.school;

import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;

import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.ArrayList;
import java.util.List;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(FacultyController.class)
class FacultyControllerMVCTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FacultyService facultyService;


    @Test
    public void testFindFaculty() throws Exception {
        String name = "testName";
        String color = "testColor";
        long facultyId = 10L;

        Faculty faculty = new Faculty(facultyId, name, color);

        when(facultyService.findFaculty(facultyId)).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + facultyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(facultyId))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));

    }


    @Test
    public void testCreateFaculty() throws Exception {
        long id = 28L;
        String name = "FacultyOne";
        String color = "ColorUno";

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyService.addFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.color").value(color))
                .andExpect(jsonPath("$.name").value(name));
    }

    @Test
    public void testEditFaculty() throws Exception {
        long id = 28L;
        String name = "FacultyOne";
        String color = "ColorUno";

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        faculty.setColor(color);

        when(facultyService.editFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.color").value(color))
                .andExpect(jsonPath("$.name").value(name));
    }

    @Test
    public void testFindFacultyByNameTest() throws Exception {
        String name = "testName";
        String color = "testColor";
        long facultyId = 10L;

        Faculty faculty = new Faculty(facultyId, name, color);
        List<Faculty> allFaculties = new ArrayList<>();
        allFaculties.add(faculty);

        when(facultyService.findByColorOrName(any(), any())).thenReturn(allFaculties);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty")
                        .param("name", name)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(facultyId))
                .andExpect(jsonPath("$[0].name").value(name))
                .andExpect(jsonPath("$[0].color").value(color));
    }

    @Test
    public void testDeleteFaculty() throws Exception {
        String name = "testName";
        String color = "testColor";
        long facultyId = 10L;

        Faculty faculty = new Faculty(facultyId, name, color);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/10"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testFindStudentByFacultyId() throws Exception {
        long id = 28L;
        String name = "StudentOne";
        int age = 13;
        String name2 = "testName";
        String color = "testColor";
        long facultyId = 10L;

        Faculty faculty = new Faculty(facultyId, name2, color);
        Student student = new Student(id, name, age, faculty);

        List<Student> students = new ArrayList<>();
        students.add(student);

        Faculty mockFaculty = mock(Faculty.class);
        when(facultyService.findFaculty(facultyId)).thenReturn(mockFaculty);
        when(mockFaculty.getStudents()).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/students/" + facultyId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].name").value(name))
                .andExpect(jsonPath("$[0].age").value(age));
    }
}
