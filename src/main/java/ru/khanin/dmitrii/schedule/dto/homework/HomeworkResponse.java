package ru.khanin.dmitrii.schedule.dto.homework;

import java.time.LocalDate;

import ru.khanin.dmitrii.schedule.dto.flow.FlowResponse;

public record HomeworkResponse(
		String homework, LocalDate lesson_date, int lesson_num, FlowResponse flow, String lesson_name
) {}
