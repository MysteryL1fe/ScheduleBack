package ru.khanin.dmitrii.schedule.dto.schedule;

import jakarta.validation.constraints.NotNull;
import ru.khanin.dmitrii.schedule.dto.flow.FlowRequest;

public record DeleteScheduleRequest(
		@NotNull FlowRequest flow, @NotNull Integer day_of_week, @NotNull Integer lesson_num,
		@NotNull Boolean is_numerator
) {}
