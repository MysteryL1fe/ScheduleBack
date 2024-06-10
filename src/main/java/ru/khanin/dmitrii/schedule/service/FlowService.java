package ru.khanin.dmitrii.schedule.service;

import java.util.Collection;

import ru.khanin.dmitrii.schedule.entity.Flow;

public interface FlowService {
	Flow add(int flowLvl, int course, int flow, int subgroup);
	Collection<Flow> findAll();
	Collection<Flow> findAllByFlowLvl(int flowLvl);
	Collection<Flow> findAllByFlowLvlAndCourse(int flowLvl, int course);
	Collection<Flow> findAllByFlowLvlAndCourseAndFlow(int flowLvl, int course, int flow);
	Flow deleteById(long id);
	Collection<Flow> deleteAll();
}
