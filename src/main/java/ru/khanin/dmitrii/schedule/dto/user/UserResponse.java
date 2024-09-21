package ru.khanin.dmitrii.schedule.dto.user;

import lombok.NonNull;

public record UserResponse(
		@NonNull String login, @NonNull Boolean admin
) {}
