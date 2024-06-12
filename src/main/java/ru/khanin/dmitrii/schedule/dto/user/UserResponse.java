package ru.khanin.dmitrii.schedule.dto.user;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import ru.khanin.dmitrii.schedule.entity.User.AccessType;

public record UserResponse(
		@NotNull Long id, @NotNull String api_key, @NotNull String name, @NotNull AccessType access,
		@Nullable long flow
) {}
