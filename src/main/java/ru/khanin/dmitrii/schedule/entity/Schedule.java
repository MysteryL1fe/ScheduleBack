package ru.khanin.dmitrii.schedule.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class Schedule {
	private long id;
	private long flow;
	private int dayOfWeek;
	private long lesson;
	private int lessonNum;
	private boolean isNumerator;
}
