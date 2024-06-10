package ru.khanin.dmitrii.schedule.dto.temp;

import java.time.LocalDate;

import ru.khanin.dmitrii.schedule.dto.flow.FlowResponse;
import ru.khanin.dmitrii.schedule.dto.lesson.LessonResponse;

public record TempScheduleResponse(
		FlowResponse flow, LessonResponse lesson, LocalDate lesson_date, int lesson_num, boolean will_lesson_be
) {}
