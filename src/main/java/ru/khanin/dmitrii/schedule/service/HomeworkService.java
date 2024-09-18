package ru.khanin.dmitrii.schedule.service;

import java.time.LocalDate;
import java.util.Collection;

import ru.khanin.dmitrii.schedule.entity.Homework;

public interface HomeworkService {
	Homework addOrUpdate(String homework, LocalDate lessonDate, int lessonNum, long flowId, long subjectId);
	Homework addOrUpdate(
			String homework, LocalDate lessonDate, int lessonNum,
			int educationLevel, int course, int group, int subgroup, String subject
	);
	Homework findById(long id);
	Homework findByLessonDateAndLessonNumAndFlow(LocalDate lessonDate, int lessonNum, long flowId);
	Homework findByLessonDateAndLessonNumAndFlow(
			LocalDate lessonDate, int lessonNum, int educationLevel, int course, int group, int subgroup
	);
	Collection<Homework> findAll();
	Collection<Homework> findAllByFlow(long flowId);
	Collection<Homework> findAllByFlow(int educationLevel, int course, int group, int subgroup);
	Collection<Homework> findAllByLessonDateAndFlow(LocalDate lessonDate, long flowId);
	Collection<Homework> findAllByLessonDateAndFlow(
			LocalDate lessonDate, int educationLevel, int course, int group, int subgroup
	);
	Collection<Homework> findAllByFlowAndSubject(long flowId, long subjectId);
	Collection<Homework> findAllByFlowAndSubject(
			int educationLevel, int course, int group, int subgroup, String subject
	);
	Homework deleteById(long id);
	Homework deleteByFlowAndLessonDateAndLessonNum(
			long flowId, LocalDate lessonDate, int lessonNum
	);
	Homework deleteByFlowAndLessonDateAndLessonNum(
			int educationLevel, int course, int group, int subgroup, LocalDate lessonDate, int lessonNum
	);
	Collection<Homework> deleteAllBeforeDate(LocalDate date);
	Collection<Homework> deleteAll();
}
