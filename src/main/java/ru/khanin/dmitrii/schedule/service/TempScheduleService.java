package ru.khanin.dmitrii.schedule.service;

import java.time.LocalDate;
import java.util.Collection;

import ru.khanin.dmitrii.schedule.entity.TempSchedule;

public interface TempScheduleService {
	TempSchedule add(long flowId, long lessonId, LocalDate lessonDate, int lessonNum, boolean willLessonBe);
	TempSchedule add(
			int flowLvl, int course, int flow, int subgroup,
			long lessonId, LocalDate lessonDate, int lessonNum, boolean willLessonBe
	);
	TempSchedule add(
			long flowId, String name, String teacher, String cabinet,
			LocalDate lessonDate, int lessonNum, boolean willLessonBe
	);
	TempSchedule add(
			int flowLvl, int course, int flow, int subgroup,
			String name, String teacher, String cabinet,
			LocalDate lessonDate, int lessonNum, boolean willLessonBe
	);
	TempSchedule findByFlowAndLessonDateAndLessonNum(long flowId, LocalDate lessonDate, int lessonNum);
	Collection<TempSchedule> findAll();
	Collection<TempSchedule> findAllByFlowAndLessonDate(long flowId, LocalDate lessonDate);
	TempSchedule deleteById(long id);
	Collection<TempSchedule> deleteAll();
	Collection<TempSchedule> deleteAllBeforeDate(LocalDate date);
}
