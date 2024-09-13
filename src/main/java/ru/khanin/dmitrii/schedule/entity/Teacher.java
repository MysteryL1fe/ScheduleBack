package ru.khanin.dmitrii.schedule.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class Teacher {
	private long id;
	private String surname;
	private String name;
	private String patronymic;
}
