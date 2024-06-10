package ru.khanin.dmitrii.schedule.service;

import java.time.LocalDate;
import java.util.Collection;

import ru.khanin.dmitrii.schedule.entity.Homework;

public interface HomeworkService {
	Homework add(String homework, LocalDate lessonDate, int lessonNum, long flowId, String lessonName);
	Homework add(
			String homework, LocalDate lessonDate, int lessonNum,
			int flowLvl, int course, int flow, int subgroup, String lessonName
	);
	Collection<Homework> findAll();
	Collection<Homework> findAllByFlow(long flowId);
	Collection<Homework> findAllByLessonDateAndFlow(LocalDate lessonDate, long flowId);
	Homework deleteById(long id);
	Collection<Homework> deleteAllBeforeDate(LocalDate date);
	Collection<Homework> deleteAll();
}
