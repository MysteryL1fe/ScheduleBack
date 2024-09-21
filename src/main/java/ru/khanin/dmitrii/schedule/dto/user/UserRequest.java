package ru.khanin.dmitrii.schedule.dto.user;

import lombok.NonNull;

public record UserRequest(
		@NonNull String login
) {}
