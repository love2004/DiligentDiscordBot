package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.living_record;

import lombok.Data;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.Bed;
import org.dyu5thdorm.dyu5thdormdiscordbot.spring.models.school_timestamp.SchoolTimestamp;

import java.io.Serializable;

@Data
public class LivingRecordId implements Serializable {
    private Bed bed;
    private SchoolTimestamp schoolTimestamp;
}