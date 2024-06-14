package ru.khanin.dmitrii.schedule.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class Schedule {
	protected long id;
	protected long flow;
	protected long lesson;
	protected int dayOfWeek;
	protected int lessonNum;
	protected boolean isNumerator;
	
	public boolean isIsNumerator() {
		return isNumerator;
	}
}
