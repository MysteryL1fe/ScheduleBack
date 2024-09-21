package ru.khanin.dmitrii.schedule.dto.temp;

import java.time.LocalDate;

import lombok.NonNull;
import ru.khanin.dmitrii.schedule.dto.cabinet.CabinetRequest;
import ru.khanin.dmitrii.schedule.dto.flow.FlowRequest;
import ru.khanin.dmitrii.schedule.dto.subject.SubjectRequest;
import ru.khanin.dmitrii.schedule.dto.teacher.TeacherRequest;

public record TempScheduleRequest(
		@NonNull FlowRequest flow, @NonNull LocalDate lesson_date, @NonNull Integer lesson_num,
		@NonNull Boolean will_lesson_be, SubjectRequest subject, TeacherRequest teacher,
		CabinetRequest cabinet
) {}
