package ru.khanin.dmitrii.schedule.dto.cabinet;

import lombok.NonNull;

public record CabinetResponse(
		@NonNull String cabinet, @NonNull String building, String address
) {}
