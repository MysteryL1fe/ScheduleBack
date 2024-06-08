package ru.khanin.dmitrii.schedule.entity;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class Homework {
	private long id;
	private String homework;
	private LocalDate lessonDate;
	private int lessonNum;
	private long flow;
	private String lessonName;
}
