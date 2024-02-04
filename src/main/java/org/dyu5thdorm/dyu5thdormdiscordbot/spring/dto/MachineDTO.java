package org.dyu5thdorm.dyu5thdormdiscordbot.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MachineDTO {
    @NotNull
    private Integer floor;
    @NotNull
    private String area;
    @NotNull
    private String bedId;
    @NotNull
    private String studentId;
    @NotNull
    private String name;
    @NotNull
    private String machineName;
    @NotNull
    private String description;
    @NotNull
    private Integer coinAmount;
    @NotNull
    private LocalDateTime eventTime;
    @NotNull
    private LocalDateTime recordTime;
}
