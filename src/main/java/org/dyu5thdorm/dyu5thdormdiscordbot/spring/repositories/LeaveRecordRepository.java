package org.dyu5thdorm.dyu5thdormdiscordbot.spring.repositories;

import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.leave.LeaveRecord;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.leave.LeaveRecordId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRecordRepository extends JpaRepository<LeaveRecord, LeaveRecordId> {
}
