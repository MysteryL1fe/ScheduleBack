package ru.khanin.dmitrii.schedule.dto.lesson;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record LessonResponse(@NotNull String name, @Nullable String teacher, @Nullable String cabinet) {}
