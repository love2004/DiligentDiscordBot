package org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.leave.LongLeave;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.leave.LongLeaveId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LongLeaveRepo extends JpaRepository<LongLeave, LongLeaveId> {
}
