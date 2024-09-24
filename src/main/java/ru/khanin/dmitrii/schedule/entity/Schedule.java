package ru.khanin.dmitrii.schedule.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class Schedule {
	protected Long id;
	protected Long flow;
	protected Long subject;
	protected Long teacher;
	protected Long cabinet;
	protected Integer dayOfWeek;
	protected Integer lessonNum;
	protected Boolean numerator;
}
