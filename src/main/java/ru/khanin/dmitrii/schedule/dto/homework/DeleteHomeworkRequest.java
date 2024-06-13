package ru.khanin.dmitrii.schedule.dto.homework;

import java.time.LocalDate;

import lombok.NonNull;
import ru.khanin.dmitrii.schedule.dto.flow.FlowRequest;

public record DeleteHomeworkRequest(
		@NonNull FlowRequest flow, @NonNull LocalDate lesson_date, @NonNull Integer lesson_num
) {}
