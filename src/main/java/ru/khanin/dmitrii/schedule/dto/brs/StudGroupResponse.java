package ru.khanin.dmitrii.schedule.dto.brs;

import java.time.LocalDate;

public record StudGroupResponse(
		int course, int num, int sub_count,
		LocalDate lessons_start_day, LocalDate session_start_date, LocalDate session_end_date
) {}
