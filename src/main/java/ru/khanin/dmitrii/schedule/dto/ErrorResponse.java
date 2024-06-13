package ru.khanin.dmitrii.schedule.dto;

import lombok.NonNull;

public record ErrorResponse(@NonNull String name, @NonNull String description) {}
