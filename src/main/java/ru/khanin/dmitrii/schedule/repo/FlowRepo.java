package ru.khanin.dmitrii.schedule.repo;

import java.util.Optional;

import ru.khanin.dmitrii.schedule.entity.Flow;

public interface FlowRepo {
	Flow add(Flow flow);
	Optional<Flow> findById(long id);
	Optional<Flow> findByFlowLvlAndCourseAndFlowAndSubgroup(int flowLvl, int course, int flow, int subgroup);
	Iterable<Flow> findAll();
	Iterable<Flow> findAllByFlowLvl(int flowLvl);
	Iterable<Flow> findAllByFlowLvlAndCourse(int flowLvl, int course);
	Iterable<Flow> findAllByFlowLvlAndCourseAndFlow(int flowLvl, int course, int flow);
	Optional<Flow> deleteById(long id);
	Iterable<Flow> deleteAll();
}
