package ru.khanin.dmitrii.schedule.dto.teacher;

import lombok.NonNull;

public record TeacherResponse(
		@NonNull String surname, String name, String patronymic
) {}
