package ru.khanin.dmitrii.schedule.dto.lesson;

import lombok.NonNull;

public record LessonResponse(@NonNull String name, String teacher, String cabinet) {}
