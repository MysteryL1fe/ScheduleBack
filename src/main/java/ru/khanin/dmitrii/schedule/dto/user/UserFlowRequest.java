package ru.khanin.dmitrii.schedule.dto.user;

import ru.khanin.dmitrii.schedule.dto.flow.FlowRequest;

public record UserFlowRequest(UserRequest user, FlowRequest flow) {}
