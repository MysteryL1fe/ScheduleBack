package ru.khanin.dmitrii.schedule.dto.schedule;

import ru.khanin.dmitrii.schedule.dto.flow.FlowResponse;
import ru.khanin.dmitrii.schedule.dto.lesson.LessonResponse;

public record ScheduleResponse(
		FlowResponse flow, LessonResponse lesson, int day_of_week, int lesson_num, boolean is_numerator
) {}
