package ru.khanin.dmitrii.schedule.dto.temp;

import java.time.LocalDate;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import ru.khanin.dmitrii.schedule.dto.flow.FlowResponse;
import ru.khanin.dmitrii.schedule.dto.lesson.LessonResponse;

public record TempScheduleResponse(
		@NotNull FlowResponse flow, @NotNull LessonResponse lesson, @NotNull LocalDate lesson_date,
		@NotNull Integer lesson_num, @Nullable boolean will_lesson_be
) {}
