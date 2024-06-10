package ru.khanin.dmitrii.schedule.dto.schedule;

import ru.khanin.dmitrii.schedule.dto.flow.FlowRequest;

public record DeleteScheduleRequest(
		FlowRequest flow, int day_of_week, int lesson_num, boolean is_numerator
) {}
