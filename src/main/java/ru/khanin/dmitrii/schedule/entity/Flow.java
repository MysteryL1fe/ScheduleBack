package ru.khanin.dmitrii.schedule.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id", callSuper = false)
public class Flow {
	protected Long id;
	protected Integer educationLevel;
	protected Integer course;
	protected Integer group;
	protected Integer subgroup;
	protected LocalDateTime lastEdit;
	protected LocalDate lessonsStartDate;
	protected LocalDate sessionStartDate;
	protected LocalDate sessionEndDate;
	protected Boolean active;
	
	public boolean equalsByFlowData(Flow other) {
		return !(other == null) && educationLevel.equals(other.educationLevel) && course.equals(other.course)
				&& group.equals(other.group) && subgroup.equals(other.subgroup);
	}
	
	public boolean equalsByDates(Flow other) {
		return !(other == null) && Objects.equals(lessonsStartDate, other.lessonsStartDate)
				&& Objects.equals(sessionStartDate, other.sessionStartDate)
				&& Objects.equals(sessionEndDate, other.sessionEndDate);
	}
}
