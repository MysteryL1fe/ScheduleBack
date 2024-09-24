package ru.khanin.dmitrii.schedule.service;

import java.util.Collection;

import ru.khanin.dmitrii.schedule.entity.Schedule;

public interface ScheduleService {
	Schedule addOrUpdate(
			int educationLevel, int course, int group, int subgroup, String subject,
			int dayOfWeek, int lessonNum, boolean numerator
	);
	Schedule addOrUpdate(
			int educationLevel, int course, int group, int subgroup, String subject,
			String surname, String name, String patronymic,
			int dayOfWeek, int lessonNum, boolean numerator
	);
	Schedule addOrUpdate(
			int educationLevel, int course, int group, int subgroup, String subject,
			String cabinet, String building, int dayOfWeek, int lessonNum, boolean numerator
	);
	Schedule addOrUpdate(
			int educationLevel, int course, int group, int subgroup, String subject,
			String surname, String name, String patronymic, String cabinet, String building,
			int dayOfWeek, int lessonNum, boolean numerator
	);
	Schedule findById(long id);
	Schedule findByFlowAndDayOfWeekAndLessonNumAndNumerator(
			int educationLevel, int course, int group, int subgroup, int dayOfWeek, int lessonNum, boolean numerator
	);
	Collection<Schedule> findAll();
	Collection<Schedule> findAllByFlow(int educationLevel, int course, int group, int subgroup);
	Collection<Schedule> findAllByFlowAndDayOfWeekAndNumerator(
			int educationLevel, int course, int group, int subgroup, int dayOfWeek, boolean numerator
	);
	Collection<Schedule> findAllByTeacher(String surname, String name, String patronymic);
	Schedule deleteById(long id);
	Schedule deleteByFlowAndDayOfWeekAndLessonNumAndNumerator(
			int educationLevel, int course, int group, int subgroup, int dayOfWeek, int lessonNum, boolean numerator
	);
	Collection<Schedule> deleteAll();
}
