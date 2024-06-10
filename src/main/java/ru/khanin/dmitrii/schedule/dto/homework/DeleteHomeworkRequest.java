package ru.khanin.dmitrii.schedule.dto.homework;

import java.time.LocalDate;

import ru.khanin.dmitrii.schedule.dto.flow.FlowRequest;

public record DeleteHomeworkRequest(FlowRequest flow, LocalDate lesson_date, int lesson_num) {}
