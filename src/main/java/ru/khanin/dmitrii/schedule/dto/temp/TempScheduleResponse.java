package ru.khanin.dmitrii.schedule.dto.temp;

import java.time.LocalDate;

import lombok.NonNull;
import ru.khanin.dmitrii.schedule.dto.cabinet.CabinetResponse;
import ru.khanin.dmitrii.schedule.dto.flow.FlowResponse;
import ru.khanin.dmitrii.schedule.dto.subject.SubjectResponse;
import ru.khanin.dmitrii.schedule.dto.teacher.TeacherResponse;

public record TempScheduleResponse(
		@NonNull FlowResponse flow, @NonNull LocalDate lesson_date, @NonNull Integer lesson_num,
		@NonNull Boolean will_lesson_be, SubjectResponse subject, TeacherResponse teacher,
		CabinetResponse cabinet
) {}
