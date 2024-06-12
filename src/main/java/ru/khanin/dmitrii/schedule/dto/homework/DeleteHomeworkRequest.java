package ru.khanin.dmitrii.schedule.dto.homework;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import ru.khanin.dmitrii.schedule.dto.flow.FlowRequest;

public record DeleteHomeworkRequest(
		@NotNull FlowRequest flow, @NotNull LocalDate lesson_date, @NotNull Integer lesson_num
) {}
