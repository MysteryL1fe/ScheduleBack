package ru.khanin.dmitrii.schedule.dto.flow;

import java.time.LocalDate;

import lombok.NonNull;

public record AddFlowRequest(
		@NonNull Integer flow_lvl, @NonNull Integer course, @NonNull Integer flow, @NonNull Integer subgroup,
		@NonNull LocalDate lessons_start_date, @NonNull LocalDate session_start_date,
		@NonNull LocalDate session_end_date
) {}
