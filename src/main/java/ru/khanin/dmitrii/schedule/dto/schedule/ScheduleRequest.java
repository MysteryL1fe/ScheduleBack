package ru.khanin.dmitrii.schedule.dto.schedule;

import ru.khanin.dmitrii.schedule.dto.flow.FlowRequest;
import ru.khanin.dmitrii.schedule.dto.lesson.LessonRequest;

public record ScheduleRequest(
		FlowRequest flow, LessonRequest lesson, int day_of_week, int lesson_num, boolean is_numerator
) {}
