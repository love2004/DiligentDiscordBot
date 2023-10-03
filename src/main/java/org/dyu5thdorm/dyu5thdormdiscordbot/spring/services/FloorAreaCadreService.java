package org.dyu5thdorm.dyu5thdormdiscordbot.spring.services;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.floor_area_cadre.FloorAreaCadre;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories.FloorAreaCadreRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FloorAreaCadreService {
    final
    FloorAreaCadreRepo floorAreaCadreRepo;

    public FloorAreaCadreService(FloorAreaCadreRepo floorAreaCadreRepo) {
        this.floorAreaCadreRepo = floorAreaCadreRepo;
    }

    public Optional<FloorAreaCadre> findByCadreStudent(Student student) {
        return floorAreaCadreRepo.findByCadre_Student(student);
    }
}
