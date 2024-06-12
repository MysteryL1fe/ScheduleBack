package ru.khanin.dmitrii.schedule.dto.temp;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import ru.khanin.dmitrii.schedule.dto.flow.FlowRequest;

public record DeleteTempScheduleRequest(
		@NotNull FlowRequest flow, @NotNull LocalDate lesson_date, @NotNull Integer lesson_num
) {}
