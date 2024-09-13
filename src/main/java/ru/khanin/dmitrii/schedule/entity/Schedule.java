package ru.khanin.dmitrii.schedule.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class Schedule {
	protected long id;
	protected long flow;
	protected long subject;
	protected long teacher;
	protected long cabinet;
	protected int dayOfWeek;
	protected int lessonNum;
	protected boolean numerator;
}
