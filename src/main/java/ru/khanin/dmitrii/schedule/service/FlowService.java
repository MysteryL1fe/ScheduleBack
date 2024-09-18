package ru.khanin.dmitrii.schedule.service;

import java.time.LocalDate;
import java.util.Collection;

import ru.khanin.dmitrii.schedule.entity.Flow;

public interface FlowService {
	Flow addOrUpdate(int educationLevel, int course, int group, int subgroup);
	Flow addOrUpdate(
			int educationLevel, int course, int group, int subgroup,
			LocalDate lessonsStartDate, LocalDate sessionStartDate, LocalDate sessionEndDate
	);
	Flow addOrUpdate(int educationLevel, int course, int group, int subgroup, boolean active);
	Flow addOrUpdate(
			int educationLevel, int course, int group, int subgroup,
			LocalDate lessonsStartDate, LocalDate sessionStartDate, LocalDate sessionEndDate,
			boolean active
	);
	Flow findById(long id);
	Flow findByEducationLevelAndCourseAndGroupAndSubgroup(int educationLevel, int course, int group, int subgroup);
	Collection<Flow> findAll();
	Collection<Flow> findAllActive();
	Collection<Flow> findAllByEducationLevel(int educationLevel);
	Collection<Flow> findAllByEducationLevelAndCourse(int educationLevel, int course);
	Collection<Flow> findAllByEducationLevelAndCourseAndGroup(int educationLevel, int course, int group);
	Flow deleteById(long id);
	Collection<Flow> deleteAll();
}
