package ru.khanin.dmitrii.schedule.entity;

import java.time.LocalDateTime;

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
	protected LocalDateTime lastEdit;
	
	public boolean equalsByFlowData(Flow other) {
		return this.flowLvl == other.flowLvl && this.course == other.course
				&& this.flow == other.flow && this.subgroup == other.subgroup;
	}
}
