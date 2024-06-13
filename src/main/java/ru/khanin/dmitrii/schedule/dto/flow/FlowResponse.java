package ru.khanin.dmitrii.schedule.dto.flow;

import java.time.LocalDateTime;

import lombok.NonNull;

public record FlowResponse(
		@NonNull Integer flow_lvl, @NonNull Integer course, @NonNull Integer flow, @NonNull Integer subgroup,
		@NonNull LocalDateTime last_edit
) {}
