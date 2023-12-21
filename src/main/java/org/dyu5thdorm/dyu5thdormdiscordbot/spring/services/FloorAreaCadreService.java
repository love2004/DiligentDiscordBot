package org.dyu5thdorm.dyu5thdormdiscordbot.spring.services;

import jakarta.annotation.PostConstruct;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.floor_area_cadre.FloorAreaCadre;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.school_timestamp.SchoolTimestamp;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.FloorAreaCadreRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FloorAreaCadreService {
    final
    FloorAreaCadreRepo floorAreaCadreRepo;
    @Value("${school_year}")
    Integer schoolYear;
    @Value("${semester}")
    Integer semester;
    SchoolTimestamp schoolTimestamp;

    @PostConstruct
    void init() {
        schoolTimestamp = new SchoolTimestamp();
        schoolTimestamp.setSchoolYear(schoolYear);
        schoolTimestamp.setSemester(semester);
    }

    public FloorAreaCadreService(FloorAreaCadreRepo floorAreaCadreRepo) {
        this.floorAreaCadreRepo = floorAreaCadreRepo;
    }

    public Optional<FloorAreaCadre> findByCadreStudent(Student student) {
        return floorAreaCadreRepo.findByCadreStudentAndCadreSchoolTimestamp(student, schoolTimestamp);
    }
}
