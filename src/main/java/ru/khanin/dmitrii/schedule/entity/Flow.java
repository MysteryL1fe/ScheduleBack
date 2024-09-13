package ru.khanin.dmitrii.schedule.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class Flow {
	protected long id;
	protected int educationLevel;
	protected int course;
	protected int group;
	protected int subgroup;
	protected LocalDateTime lastEdit;
	protected LocalDate lessonsStartDate;
	protected LocalDate sessionStartDate;
	protected LocalDate sessionEndDate;
	protected boolean active;
	
	public boolean equalsByFlowData(Flow other) {
		return !(other == null) && this.educationLevel == other.educationLevel && this.course == other.course
				&& this.group == other.group && this.subgroup == other.subgroup;
	}
	
	public boolean equalsByDates(Flow other) {
		return !(other == null) && Objects.equals(lessonsStartDate, other.lessonsStartDate)
				&& Objects.equals(sessionStartDate, other.sessionStartDate)
				&& Objects.equals(sessionEndDate, other.sessionEndDate);
	}
}
