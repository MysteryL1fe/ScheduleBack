package ru.khanin.dmitrii.schedule.entity;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class Homework {
	protected long id;
	protected String homework;
	protected LocalDate lessonDate;
	protected int lessonNum;
	protected long flow;
	protected long subject;
}
