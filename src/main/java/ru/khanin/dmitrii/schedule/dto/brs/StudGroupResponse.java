package ru.khanin.dmitrii.schedule.dto.brs;

import java.time.LocalDate;

import lombok.NonNull;

public record StudGroupResponse(
		@NonNull Integer course, @NonNull Integer num, @NonNull Integer sub_count,
		@NonNull LocalDate lessons_start_day, @NonNull LocalDate session_start_date,
		@NonNull LocalDate session_end_date
) {}
