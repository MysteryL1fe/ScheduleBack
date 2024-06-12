package ru.khanin.dmitrii.schedule.dto.user;

import ru.khanin.dmitrii.schedule.entity.User.AccessType;

public record UserResponse(long id, String api_key, String name, AccessType access, long flow) {}
