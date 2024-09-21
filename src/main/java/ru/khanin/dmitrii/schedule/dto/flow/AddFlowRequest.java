package ru.khanin.dmitrii.schedule.dto.flow;

import java.time.LocalDate;

import lombok.NonNull;

public record AddFlowRequest(
		@NonNull Integer education_level, @NonNull Integer course, @NonNull Integer group, @NonNull Integer subgroup,
		LocalDate lessons_start_date, LocalDate session_start_date, LocalDate session_end_date, @NonNull Boolean active
) {}
