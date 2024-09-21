package ru.khanin.dmitrii.schedule.dto.schedule;

import lombok.NonNull;
import ru.khanin.dmitrii.schedule.dto.cabinet.CabinetRequest;
import ru.khanin.dmitrii.schedule.dto.flow.FlowRequest;
import ru.khanin.dmitrii.schedule.dto.subject.SubjectRequest;
import ru.khanin.dmitrii.schedule.dto.teacher.TeacherRequest;

public record ScheduleRequest(
		@NonNull FlowRequest flow, @NonNull Integer day_of_week, @NonNull Integer lesson_num,
		@NonNull Boolean numerator, @NonNull SubjectRequest subject, TeacherRequest teacher,
		CabinetRequest cabinet
) {}
