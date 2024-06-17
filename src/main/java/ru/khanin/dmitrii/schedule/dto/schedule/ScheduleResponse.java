package ru.khanin.dmitrii.schedule.dto.schedule;

import lombok.NonNull;
import ru.khanin.dmitrii.schedule.dto.flow.FlowResponse;
import ru.khanin.dmitrii.schedule.dto.lesson.LessonResponse;

public record ScheduleResponse(
		@NonNull FlowResponse flow, @NonNull LessonResponse lesson, @NonNull Integer day_of_week,
		@NonNull Integer lesson_num, @NonNull Boolean numerator
) {}
