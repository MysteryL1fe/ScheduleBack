package ru.khanin.dmitrii.schedule.dto.brs;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public record ListStudGroupsResponse(@NotNull List<StudGroupResponse> stud_groups) {}
