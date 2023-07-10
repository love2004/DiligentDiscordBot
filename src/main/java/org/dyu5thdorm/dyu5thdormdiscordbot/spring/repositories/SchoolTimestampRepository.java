package org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.SchoolTimestamp;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.SchoolTimestampId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolTimestampRepository extends JpaRepository<SchoolTimestamp, SchoolTimestampId> {
}
