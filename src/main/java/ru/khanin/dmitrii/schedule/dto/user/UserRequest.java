package ru.khanin.dmitrii.schedule.dto.user;

import lombok.NonNull;
import ru.khanin.dmitrii.schedule.dto.flow.FlowRequest;
import ru.khanin.dmitrii.schedule.entity.User.AccessType;

public record UserRequest(
		@NonNull String api_key, @NonNull String name, @NonNull AccessType access, FlowRequest flow
) {}
