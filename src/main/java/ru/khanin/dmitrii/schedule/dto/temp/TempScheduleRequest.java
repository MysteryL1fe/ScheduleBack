package ru.khanin.dmitrii.schedule.dto.temp;

import java.time.LocalDate;

import lombok.NonNull;
import ru.khanin.dmitrii.schedule.dto.flow.FlowRequest;
import ru.khanin.dmitrii.schedule.dto.lesson.LessonResponse;

public record TempScheduleRequest(
		@NonNull FlowRequest flow, @NonNull LessonResponse lesson, @NonNull LocalDate lesson_date,
		@NonNull Integer lesson_num, @NonNull Boolean will_lesson_be
) {}
