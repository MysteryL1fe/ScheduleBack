package ru.khanin.dmitrii.schedule.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class Lesson {
	protected long id;
	protected String name;
	protected String teacher;
	protected String cabinet;
}
