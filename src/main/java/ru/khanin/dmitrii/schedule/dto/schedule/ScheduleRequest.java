package ru.khanin.dmitrii.schedule.dto.schedule;

import jakarta.validation.constraints.NotNull;
import ru.khanin.dmitrii.schedule.dto.flow.FlowRequest;
import ru.khanin.dmitrii.schedule.dto.lesson.LessonRequest;

public record ScheduleRequest(
		@NotNull FlowRequest flow, @NotNull LessonRequest lesson, @NotNull Integer day_of_week,
		@NotNull Integer lesson_num, @NotNull Boolean is_numerator
) {}
