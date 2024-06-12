package ru.khanin.dmitrii.schedule.dto.flow;

import jakarta.validation.constraints.NotNull;

public record FlowResponse(
		@NotNull Integer flow_lvl, @NotNull Integer course, @NotNull Integer flow, @NotNull Integer subgroup
) {}
