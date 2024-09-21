package ru.khanin.dmitrii.schedule.dto.cabinet;

import lombok.NonNull;

public record CabinetRequest(
		@NonNull String cabinet, @NonNull String building, String address
) {}
