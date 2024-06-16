package ru.khanin.dmitrii.schedule.service;

import java.time.LocalDate;
import java.util.Collection;

import ru.khanin.dmitrii.schedule.entity.Flow;

public interface FlowService {
	Flow addOrUpdate(int flowLvl, int course, int flow, int subgroup);
	Flow addOrUpdate(
			int flowLvl, int course, int flow, int subgroup,
			LocalDate lessonsStartDate, LocalDate sessionStartDate, LocalDate sessionEndDate
	);
	Flow addOrUpdate(int flowLvl, int course, int flow, int subgroup, boolean active);
	Flow addOrUpdate(
			int flowLvl, int course, int flow, int subgroup,
			LocalDate lessonsStartDate, LocalDate sessionStartDate, LocalDate sessionEndDate,
			boolean active
	);
	Flow findById(long id);
	Flow findByFlowLvlAndCourseAndFlowAndSubgroup(int flowLvl, int course, int flow, int subgroup);
	Collection<Flow> findAll();
	Collection<Flow> findAllActive();
	Collection<Flow> findAllByFlowLvl(int flowLvl);
	Collection<Flow> findAllByFlowLvlAndCourse(int flowLvl, int course);
	Collection<Flow> findAllByFlowLvlAndCourseAndFlow(int flowLvl, int course, int flow);
	Flow deleteById(long id);
	Collection<Flow> deleteAll();
}
