package ru.khanin.dmitrii.schedule.dto.flow;

import lombok.NonNull;

public record FlowResponse(
		@NonNull Integer flow_lvl, @NonNull Integer course, @NonNull Integer flow, @NonNull Integer subgroup
) {}
