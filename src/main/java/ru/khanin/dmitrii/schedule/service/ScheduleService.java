package ru.khanin.dmitrii.schedule.service;

import java.util.Collection;

import ru.khanin.dmitrii.schedule.entity.Schedule;

public interface ScheduleService {
	Schedule add(long flowId, int dayOfWeek, long lessonId, int lessonNum, boolean isNumerator);
	Schedule add(
			int flowLvl, int course, int flow, int subgroup, int dayOfWeek,
			long lessonId, int lessonNum, boolean isNumerator
	);
	Schedule add(
			long flowId, int dayOfWeek, String name, String teacher, String cabinet,
			int lessonNum, boolean isNumerator
	);
	Schedule add(
			int flowLvl, int course, int flow, int subgroup, int dayOfWeek,
			String name, String teacher, String cabinet, int lessonNum, boolean isNumerator
	);
	Schedule findByFlowAndDayOfWeekAndLessonNumAndIsNumerator(
			long flowId, int dayOfWeek, int lessonNum, boolean isNumerator
	);
	Collection<Schedule> findAll();
	Collection<Schedule> findAllByFlow(long flowId);
	Collection<Schedule> findAllByFlowAndDayOfWeekAndIsNumerator(
			long flowId, int dayOfWeek, boolean isNumerator
	);
	Schedule deleteById(long id);
	Collection<Schedule> deleteAll();
}
