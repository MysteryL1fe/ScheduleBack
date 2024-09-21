package ru.khanin.dmitrii.schedule.dto.flow;

import lombok.NonNull;

public record FlowRequest(
		@NonNull Integer education_level, @NonNull Integer course, @NonNull Integer group, @NonNull Integer subgroup
) {}
