package ru.khanin.dmitrii.schedule.service;

import java.util.Collection;

import ru.khanin.dmitrii.schedule.entity.Schedule;

public interface ScheduleService {
	Schedule addOrUpdate(long flowId, long lessonId, int dayOfWeek, int lessonNum, boolean numerator);
	Schedule addOrUpdate(
			int flowLvl, int course, int flow, int subgroup, long lessonId,
			int dayOfWeek, int lessonNum, boolean numerator
	);
	Schedule addOrUpdate(
			long flowId, String name, String teacher, String cabinet, int dayOfWeek,
			int lessonNum, boolean numerator
	);
	Schedule addOrUpdate(
			int flowLvl, int course, int flow, int subgroup,
			String name, String teacher, String cabinet,
			int dayOfWeek, int lessonNum, boolean numerator
	);
	Schedule findByFlowAndDayOfWeekAndLessonNumAndNumerator(
			long flowId, int dayOfWeek, int lessonNum, boolean numerator
	);
	Collection<Schedule> findAll();
	Collection<Schedule> findAllByFlow(long flowId);
	Collection<Schedule> findAllByFlow(int flowLvl, int course, int flow, int subgroup);
	Collection<Schedule> findAllByFlowAndDayOfWeekAndNumerator(
			long flowId, int dayOfWeek, boolean numerator
	);
	Collection<Schedule> findAllWhereTeacherStartsWith(String teacher);
	Schedule delete(int flowLvl, int course, int flow, int subgroup, int dayOfWeek, int lessonNum, boolean numerator);
	Schedule deleteById(long id);
	Collection<Schedule> deleteAll();
}
