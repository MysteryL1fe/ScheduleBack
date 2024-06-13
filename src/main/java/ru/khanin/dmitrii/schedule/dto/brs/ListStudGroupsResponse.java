package ru.khanin.dmitrii.schedule.dto.brs;

import java.util.List;

import lombok.NonNull;

public record ListStudGroupsResponse(@NonNull List<StudGroupResponse> stud_groups) {}
