package ru.hogwarts.school;

import net.minidev.json.JSONObject;
import org.hibernate.mapping.Collection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerMVCTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;


    @Test
    public void testAddStudent() throws Exception {
        long id = 28L;
        String name = "StudentOne";
        int age = 13;

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        studentObject.put("age", age);


        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);

        when(studentService.addStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.age").value(age))
                .andExpect(jsonPath("$.name").value(name));
    }

    @Test
    public void testEditStudent() throws Exception {
        long id = 28L;
        String name = "StudentOne";
        int age = 13;

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        studentObject.put("age", age);

        Student student = new Student();
        student.setId(id);
        student.setName(name);
        student.setAge(age);


        when(studentService.editStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.age").value(age))
                .andExpect(jsonPath("$.name").value(name));
    }

    @Test
    public void testDeleteStudent() throws Exception {
        long id = 28L;
        String name = "StudentOne";
        int age = 13;
        String name2 = "testName";
        String color = "testColor";
        long facultyId = 10L;

        Faculty faculty = new Faculty(facultyId, name2, color);
        Student student = new Student(id, name, age, faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/28"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testGetStudentById() throws Exception {
        long id = 28L;
        String name = "StudentOne";
        int age = 13;
        String name2 = "testName";
        String color = "testColor";
        long facultyId = 10L;

        Faculty faculty = new Faculty(facultyId, name2, color);
        Student student = new Student(id, name, age, faculty);

        when(studentService.findStudent(id)).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.age").value(age));
    }

    @Test
    public void testFindStudentByAge() throws Exception {
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

        when(studentService.findByAge(age)).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student")
                        .param("age", "13"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].name").value(name))
                .andExpect(jsonPath("$[0].age").value(age));
    }

    @Test
    public void testFindStudentByAgeBetween() throws Exception {
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

        when(studentService.findByAgeBetween(12, 14)).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/ageBetween")
                        .param("minAge", "12")
                        .param("maxAge", "14"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].name").value(name))
                .andExpect(jsonPath("$[0].age").value(age));
    }

    @Test
    public void testFindFacultyByStudentId() throws Exception {
        long id = 28L;
        String name = "StudentOne";
        int age = 13;
        String name2 = "testName";
        String color = "testColor";
        long facultyId = 10L;

        Faculty faculty = new Faculty(facultyId, name2, color);
        Student student = new Student(id, name, age, faculty);

        when(studentService.findFacultyByStudentId(id)).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/faculty/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(facultyId))
                .andExpect(jsonPath("$.name").value(name2))
                .andExpect(jsonPath("$.color").value(color));
    }

}
