package ru.khanin.dmitrii.schedule.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class Flow {
	private long id;
	private int flowLvl;
	private int course;
	private int flow;
	private int subgroup;
}
