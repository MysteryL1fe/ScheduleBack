package ru.khanin.dmitrii.schedule.dto.user;

import lombok.NonNull;
import ru.khanin.dmitrii.schedule.entity.User.AccessType;

public record UserResponse(
		@NonNull Long id, @NonNull String api_key, @NonNull String name, @NonNull AccessType access,
		Long flow
) {}
