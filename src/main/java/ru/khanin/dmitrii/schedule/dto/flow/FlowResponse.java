package ru.khanin.dmitrii.schedule.dto.flow;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.NonNull;

public record FlowResponse(
		@NonNull Integer education_level, @NonNull Integer course, @NonNull Integer group, @NonNull Integer subgroup,
		@NonNull LocalDateTime last_edit, LocalDate lessons_start_date, LocalDate session_start_date,
		LocalDate session_end_date, @NonNull Boolean active
) {}
