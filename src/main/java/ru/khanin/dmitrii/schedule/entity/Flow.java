package ru.khanin.dmitrii.schedule.entity;

import java.time.LocalDate;
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
	protected LocalDate lessonsStartDate;
	protected LocalDate sessionStartDate;
	protected LocalDate sessionEndDate;
	
	public boolean equalsByFlowData(Flow other) {
		return this.flowLvl == other.flowLvl && this.course == other.course
				&& this.flow == other.flow && this.subgroup == other.subgroup;
	}
	
	public boolean equalsByDates(Flow other) {
		return this.lessonsStartDate.equals(other.lessonsStartDate)
				&& this.sessionStartDate.equals(other.sessionStartDate)
				&& this.sessionEndDate.equals(other.sessionEndDate);
	}
}
