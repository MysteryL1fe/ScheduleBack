package ru.khanin.dmitrii.schedule.repo;

import java.util.Optional;

import ru.khanin.dmitrii.schedule.entity.Flow;

public interface FlowRepo {
	Flow add(Flow flow);
	Flow update(Flow flow);
	Optional<Flow> findById(long id);
	Optional<Flow> findByEducationLevelAndCourseAndGroupAndSubgroup(int educationLevel, int course, int group, int subgroup);
	Iterable<Flow> findAll();
	Iterable<Flow> findAllActive();
	Iterable<Flow> findAllByEducationLevel(int educationLevel);
	Iterable<Flow> findAllByEducationLevelAndCourse(int educationLevel, int group);
	Iterable<Flow> findAllByEducationLevelAndCourseAndGroup(int educationLevel, int group, int flow);
	Optional<Flow> deleteById(long id);
	Iterable<Flow> deleteAll();
}
