package ru.khanin.dmitrii.schedule.repo;

import java.time.LocalDate;
import java.util.Optional;

import ru.khanin.dmitrii.schedule.entity.TempSchedule;

public interface TempScheduleRepo {
	TempSchedule add(TempSchedule tempSchedule);
	TempSchedule update(TempSchedule tempSchedule);
	Optional<TempSchedule> findById(long id);
	Optional<TempSchedule> findByFlowAndLessonDateAndLessonNum(long flow, LocalDate lessonDate, int lessonNum);
	Iterable<? extends TempSchedule> findAll();
	Iterable<? extends TempSchedule> findAllByFlow(long flow);
	Iterable<TempSchedule> findAllByFlowAndLessonDate(long flow, LocalDate lessonDate);
	Optional<TempSchedule> deleteById(long id);
	Iterable<TempSchedule> deleteAll();
	Iterable<TempSchedule> deleteAllBeforeDate(LocalDate date);
	Iterable<TempSchedule> deleteAllByFlow(long flow);
	Iterable<TempSchedule> deleteAllByLesson(long lesson);
}
