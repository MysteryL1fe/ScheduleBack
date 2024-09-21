package ru.khanin.dmitrii.schedule.dto.schedule;

import lombok.NonNull;
import ru.khanin.dmitrii.schedule.dto.cabinet.CabinetResponse;
import ru.khanin.dmitrii.schedule.dto.flow.FlowResponse;
import ru.khanin.dmitrii.schedule.dto.subject.SubjectResponse;
import ru.khanin.dmitrii.schedule.dto.teacher.TeacherResponse;

public record ScheduleResponse(
		@NonNull FlowResponse flow, @NonNull Integer day_of_week, @NonNull Integer lesson_num,
		@NonNull Boolean numerator, @NonNull SubjectResponse subject, TeacherResponse teacher,
		CabinetResponse cabinet
) {}
