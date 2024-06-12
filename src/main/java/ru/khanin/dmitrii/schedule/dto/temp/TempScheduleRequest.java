package ru.khanin.dmitrii.schedule.dto.temp;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import ru.khanin.dmitrii.schedule.dto.flow.FlowResponse;
import ru.khanin.dmitrii.schedule.dto.lesson.LessonResponse;

public record TempScheduleRequest(
		@NotNull FlowResponse flow, @NotNull LessonResponse lesson, @NotNull LocalDate lesson_date,
		@NotNull Integer lesson_num, @NotNull Boolean will_lesson_be
) {}
