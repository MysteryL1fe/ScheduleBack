package ru.khanin.dmitrii.schedule.dto.user;

import ru.khanin.dmitrii.schedule.dto.flow.FlowRequest;
import ru.khanin.dmitrii.schedule.entity.User.AccessType;

public record UserRequest(
		String api_key, String name, AccessType access, FlowRequest flow
) {}
