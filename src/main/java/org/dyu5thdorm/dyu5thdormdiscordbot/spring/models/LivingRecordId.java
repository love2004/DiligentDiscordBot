package org.dyu5thdorm.dyu5thdormdiscordbot.spring.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component
public class LivingRecordId implements Serializable {
    private Bed bed;
    private SchoolTimestamp schoolTimestamp;
}