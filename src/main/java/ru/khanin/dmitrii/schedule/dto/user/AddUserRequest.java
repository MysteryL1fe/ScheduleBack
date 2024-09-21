package ru.khanin.dmitrii.schedule.dto.user;

import lombok.NonNull;

public record AddUserRequest(
		@NonNull String login, @NonNull String password, @NonNull Boolean admin
) {}
