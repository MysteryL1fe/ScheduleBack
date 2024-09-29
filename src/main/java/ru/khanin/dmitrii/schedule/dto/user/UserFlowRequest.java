package ru.khanin.dmitrii.schedule.dto.user;

import lombok.NonNull;
import ru.khanin.dmitrii.schedule.dto.flow.FlowRequest;

public record UserFlowRequest(@NonNull UserRequest user, @NonNull FlowRequest flow) {}
