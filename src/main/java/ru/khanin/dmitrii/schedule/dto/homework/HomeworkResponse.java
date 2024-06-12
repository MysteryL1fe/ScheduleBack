package ru.khanin.dmitrii.schedule.dto.homework;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import ru.khanin.dmitrii.schedule.dto.flow.FlowResponse;

public record HomeworkResponse(
		@NotNull String homework, @NotNull LocalDate lesson_date, @NotNull Integer lesson_num,
		@NotNull FlowResponse flow, @NotNull String lesson_name
) {}
