package org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Student;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.floor_area_cadre.FloorAreaCadre;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.floor_area_cadre.FloorAreaCadreId;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.school_timestamp.SchoolTimestamp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FloorAreaCadreRepo extends JpaRepository<FloorAreaCadre, FloorAreaCadreId> {
    Optional<FloorAreaCadre> findByCadreStudentAndCadreSchoolTimestamp(Student cadre_student, SchoolTimestamp cadre_schoolTimestamp);
}
