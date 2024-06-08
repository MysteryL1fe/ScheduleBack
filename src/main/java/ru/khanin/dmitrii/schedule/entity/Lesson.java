package ru.khanin.dmitrii.schedule.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class Lesson {
	private long id;
	private String name;
	private String teacher;
	private String cabinet;
}
