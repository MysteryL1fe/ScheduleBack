package ru.khanin.dmitrii.schedule.service;

import java.time.LocalDate;
import java.util.Collection;

import ru.khanin.dmitrii.schedule.entity.TempSchedule;

public interface TempScheduleService {
	TempSchedule addOrUpdate(long flowId, LocalDate lessonDate, int lessonNum, boolean willLessonBe);
	TempSchedule addOrUpdate(
			int educationLevel, int course, int group, int subgroup,
			LocalDate lessonDate, int lessonNum, boolean willLessonBe
	);
	TempSchedule addOrUpdate(
			long flowId, long subjectId, LocalDate lessonDate, int lessonNum, boolean willLessonBe
	);
	TempSchedule addOrUpdate(
			int educationLevel, int course, int group, int subgroup, String subject,
			LocalDate lessonDate, int lessonNum, boolean willLessonBe
	);
	TempSchedule addOrUpdate(
			long flowId, long subjectId, long teacherId, long cabinetId,
			LocalDate lessonDate, int lessonNum, boolean willLessonBe
	);
	TempSchedule addOrUpdate(
			int educationLevel, int course, int group, int subgroup, String subject,
			String surname, String name, String patronymic, String cabinet, String building,
			LocalDate lessonDate, int lessonNum, boolean willLessonBe
	);
	TempSchedule findById(long id);
	TempSchedule findByFlowAndLessonDateAndLessonNum(long flowId, LocalDate lessonDate, int lessonNum);
	TempSchedule findByFlowAndLessonDateAndLessonNum(
			int educationLevel, int course, int group, int subgroup, LocalDate lessonDate, int lessonNum
	);
	Collection<TempSchedule> findAll();
	Collection<TempSchedule> findAllByFlow(long flowId);
	Collection<TempSchedule> findAllByFlow(int educationLevel, int course, int group, int subgroup);
	Collection<TempSchedule> findAllByFlowAndLessonDate(long flowId, LocalDate lessonDate);
	Collection<TempSchedule> findAllByFlowAndLessonDate(
			int educationLevel, int course, int group, int subgroup, LocalDate lessonDate
	);
	TempSchedule deleteByFlowAndLessonDateAndLessonNum(
			long flowId, LocalDate lessonDate, int lessonNum
	);
	TempSchedule deleteByFlowAndLessonDateAndLessonNum(
			int educationLevel, int course, int group, int subgroup, LocalDate lessonDate, int lessonNum
	);
	TempSchedule deleteById(long id);
	Collection<TempSchedule> deleteAll();
	Collection<TempSchedule> deleteAllBeforeDate(LocalDate date);
}
