package ru.khanin.dmitrii.schedule.dto.schedule;

import lombok.NonNull;
import ru.khanin.dmitrii.schedule.dto.flow.FlowRequest;

public record DeleteScheduleRequest(
		@NonNull FlowRequest flow, @NonNull Integer day_of_week, @NonNull Integer lesson_num,
		@NonNull Boolean numerator
) {}
