package ru.khanin.dmitrii.schedule.dto.subject;

import lombok.NonNull;

public record SubjectRequest(
		@NonNull String subject
) {}
