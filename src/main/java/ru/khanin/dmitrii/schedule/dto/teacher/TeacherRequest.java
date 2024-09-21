package ru.khanin.dmitrii.schedule.dto.teacher;

import lombok.NonNull;

public record TeacherRequest(
		@NonNull String surname, String name, String patronymic
) {}
