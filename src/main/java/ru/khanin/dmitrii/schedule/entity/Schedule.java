package ru.khanin.dmitrii.schedule.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class Schedule {
	private long id;
	private long flow;
	private long lesson;
	private int dayOfWeek;
	private int lessonNum;
	private boolean isNumerator;
}
