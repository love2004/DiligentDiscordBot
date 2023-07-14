package org.dyu5thdorm.dyu5thdormdiscordbot.discrod.events.repair;

import jakarta.annotation.Nullable;

public record RepairMessageModel(
        String studentName,
        String studentId,
        String studentBedId,
        String studentPhoneNumber,
        String repairItems,
        String repairReason,
        @Nullable String repairTime
) {}
