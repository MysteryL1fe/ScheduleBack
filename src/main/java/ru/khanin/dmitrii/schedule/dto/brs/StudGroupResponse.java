package ru.khanin.dmitrii.schedule.dto.brs;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record StudGroupResponse(
		@NotNull Integer course, @NotNull Integer num, @NotNull Integer sub_count,
		@NotNull LocalDate lessons_start_day, @NotNull LocalDate session_start_date,
		@NotNull LocalDate session_end_date
) {}
