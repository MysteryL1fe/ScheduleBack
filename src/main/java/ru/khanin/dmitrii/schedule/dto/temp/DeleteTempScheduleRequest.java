package ru.khanin.dmitrii.schedule.dto.temp;

import java.time.LocalDate;

import ru.khanin.dmitrii.schedule.dto.flow.FlowRequest;

public record DeleteTempScheduleRequest(FlowRequest flow, LocalDate lesson_date, int lesson_num) {}
