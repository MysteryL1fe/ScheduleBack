package ru.khanin.dmitrii.schedule.dto.cabinet;

import lombok.NonNull;

public record DeleteCabinetRequest(
		@NonNull String cabinet, @NonNull String building
) {}
