package ru.khanin.dmitrii.schedule.dto.homework;

import java.time.LocalDate;

import lombok.NonNull;
import ru.khanin.dmitrii.schedule.dto.flow.FlowResponse;

public record HomeworkResponse(
		@NonNull String homework, @NonNull LocalDate lesson_date, @NonNull Integer lesson_num,
		@NonNull FlowResponse flow, @NonNull String lesson_name
) {}
