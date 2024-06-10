package ru.khanin.dmitrii.schedule.service;

import java.util.Collection;

import ru.khanin.dmitrii.schedule.entity.Schedule;

public interface ScheduleService {
	Schedule add(long flowId, long lessonId, int dayOfWeek, int lessonNum, boolean isNumerator);
	Schedule add(
			int flowLvl, int course, int flow, int subgroup, long lessonId,
			int dayOfWeek, int lessonNum, boolean isNumerator
	);
	Schedule add(
			long flowId, String name, String teacher, String cabinet, int dayOfWeek,
			int lessonNum, boolean isNumerator
	);
	Schedule add(
			int flowLvl, int course, int flow, int subgroup,
			String name, String teacher, String cabinet,
			int dayOfWeek, int lessonNum, boolean isNumerator
	);
	Schedule findByFlowAndDayOfWeekAndLessonNumAndIsNumerator(
			long flowId, int dayOfWeek, int lessonNum, boolean isNumerator
	);
	Collection<Schedule> findAll();
	Collection<Schedule> findAllByFlow(long flowId);
	Collection<Schedule> findAllByFlow(int flowLvl, int course, int flow, int subgroup);
	Collection<Schedule> findAllByFlowAndDayOfWeekAndIsNumerator(
			long flowId, int dayOfWeek, boolean isNumerator
	);
	Collection<Schedule> findAllWhereTeacherStartsWith(String teacher);
	Schedule delete(int flowLvl, int course, int flow, int subgroup, int dayOfWeek, int lessonNum, boolean isNumerator);
	Schedule deleteById(long id);
	Collection<Schedule> deleteAll();
}
