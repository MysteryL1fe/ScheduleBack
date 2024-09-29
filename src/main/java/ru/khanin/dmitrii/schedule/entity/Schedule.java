package ru.khanin.dmitrii.schedule.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class Schedule {
	protected Long id;
	protected Flow flow;
	protected Subject subject;
	protected Teacher teacher;
	protected Cabinet cabinet;
	protected Integer dayOfWeek;
	protected Integer lessonNum;
	protected Boolean numerator;
}
