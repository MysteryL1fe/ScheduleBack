package ru.khanin.dmitrii.schedule.dto.schedule;

import lombok.NonNull;
import ru.khanin.dmitrii.schedule.dto.flow.FlowRequest;
import ru.khanin.dmitrii.schedule.dto.lesson.LessonRequest;

public record ScheduleRequest(
		@NonNull FlowRequest flow, @NonNull LessonRequest lesson, @NonNull Integer day_of_week,
		@NonNull Integer lesson_num, @NonNull Boolean numerator
) {}
