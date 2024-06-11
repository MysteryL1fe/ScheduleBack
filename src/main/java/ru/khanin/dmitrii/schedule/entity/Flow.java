package ru.khanin.dmitrii.schedule.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class Flow {
	protected long id;
	protected int flowLvl;
	protected int course;
	protected int flow;
	protected int subgroup;
}
