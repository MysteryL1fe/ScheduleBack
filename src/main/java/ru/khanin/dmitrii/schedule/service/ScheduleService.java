package ru.khanin.dmitrii.schedule.service;

import java.util.Collection;

import ru.khanin.dmitrii.schedule.entity.Schedule;

public interface ScheduleService {
	Schedule addOrUpdate(
			long flowId, long subjectId,
			int dayOfWeek, int lessonNum, boolean numerator
	);
	Schedule addOrUpdate(
			int educationLevel, int course, int group, int subgroup, String subject,
			int dayOfWeek, int lessonNum, boolean numerator
	);
	Schedule addOrUpdate(
			long flowId, long subjectId, long teacherId, long cabinetId,
			int dayOfWeek, int lessonNum, boolean numerator
	);
	Schedule addOrUpdate(
			int educationLevel, int course, int group, int subgroup, String subject,
			String surname, String name, String patronymic, String cabinet, String building,
			int dayOfWeek, int lessonNum, boolean numerator
	);
	Schedule findById(long id);
	Schedule findByFlowAndDayOfWeekAndLessonNumAndNumerator(
			long flowId, int dayOfWeek, int lessonNum, boolean numerator
	);
	Schedule findByFlowAndDayOfWeekAndLessonNumAndNumerator(
			int educationLevel, int course, int group, int subgroup, int dayOfWeek, int lessonNum, boolean numerator
	);
	Collection<Schedule> findAll();
	Collection<Schedule> findAllByFlow(long flowId);
	Collection<Schedule> findAllByFlow(int educationLevel, int course, int group, int subgroup);
	Collection<Schedule> findAllByFlowAndDayOfWeekAndNumerator(
			long flowId, int dayOfWeek, boolean numerator
	);
	Collection<Schedule> findAllByFlowAndDayOfWeekAndNumerator(
			int educationLevel, int course, int group, int subgroup, int dayOfWeek, boolean numerator
	);
	Collection<Schedule> findAllByTeacher(long teacherId);
	Collection<Schedule> findAllByTeacher(String surname, String name, String patronymic);
	Collection<Schedule> findAllWhereTeacherFullnameStartsWith(String fullname);
	Schedule deleteById(long id);
	Schedule deleteByFlowAndDayOfWeekAndLessonNumAndNumerator(
			long flowId, int dayOfWeek, int lessonNum, boolean numerator
	);
	Schedule deleteByFlowAndDayOfWeekAndLessonNumAndNumerator(
			int educationLevel, int course, int group, int subgroup, int dayOfWeek, int lessonNum, boolean numerator
	);
	Collection<Schedule> deleteAll();
}
