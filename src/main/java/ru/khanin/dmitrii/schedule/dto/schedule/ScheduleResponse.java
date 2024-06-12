package ru.khanin.dmitrii.schedule.dto.schedule;

import jakarta.validation.constraints.NotNull;
import ru.khanin.dmitrii.schedule.dto.flow.FlowResponse;
import ru.khanin.dmitrii.schedule.dto.lesson.LessonResponse;

public record ScheduleResponse(
		@NotNull FlowResponse flow, @NotNull LessonResponse lesson, @NotNull Integer day_of_week,
		@NotNull Integer lesson_num, @NotNull Boolean is_numerator
) {}
