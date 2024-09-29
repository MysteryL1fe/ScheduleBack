package ru.khanin.dmitrii.schedule.entity;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class Homework {
	protected Long id;
	protected String homework;
	protected LocalDate lessonDate;
	protected Integer lessonNum;
	protected Flow flow;
	protected Subject subject;
}
