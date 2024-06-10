package ru.khanin.dmitrii.schedule.dto.homework;

import java.time.LocalDate;

import ru.khanin.dmitrii.schedule.dto.flow.FlowRequest;

public record HomeworkRequest(
		String homework, LocalDate lesson_date, int lesson_num, FlowRequest flow, String lesson_name
) {}
