package ru.khanin.dmitrii.schedule.dto.user;

import java.util.List;

import lombok.NonNull;
import ru.khanin.dmitrii.schedule.dto.flow.FlowResponse;

public record UserResponse(
		@NonNull String login, @NonNull Boolean admin, List<FlowResponse> flows
) {}
