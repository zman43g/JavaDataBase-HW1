package ru.hogwarts.school.service;


import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import ru.hogwarts.school.repository.FacultyRepository;

@Service
public class FacultyService {
    Logger logger = LoggerFactory.getLogger(FacultyService.class);

    @Autowired
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        logger.info("Was invoked addFaculty method for Faculty {}", faculty);
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(long id) {
        logger.info("Was invoked findFaculty method for id {}",id);
        return facultyRepository.findById(id).get();
    }

    public Faculty editFaculty(Faculty faculty) {
        logger.info("Was invoked editFaculty method for Faculty {}", faculty);
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(long id) {
        logger.info("Was invoked deleteFaculty method for id {}",id);
        facultyRepository.deleteById(id);
    }

    public List<Faculty> findByColorOrName(String color, String name) {
        logger.info("Was invoked findByColorOrName method for color {}, and name {}", color, name);
        return facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(color, name);
    }

    public String longestFacultyName() {
        logger.info("Was invoked longestFacultyName method");
        return facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length)).get();
    }


}