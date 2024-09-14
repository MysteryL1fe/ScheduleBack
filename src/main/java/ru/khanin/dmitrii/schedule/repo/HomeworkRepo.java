package ru.khanin.dmitrii.schedule.repo;

import java.time.LocalDate;
import java.util.Optional;

import ru.khanin.dmitrii.schedule.entity.Homework;

public interface HomeworkRepo {
	Homework add(Homework homework);
	Homework update(Homework homework);
	Optional<Homework> findById(long id);
	Optional<Homework> findByLessonDateAndLessonNumAndFlow(LocalDate lessonDate, int lessonNum, long flow);
	Iterable<? extends Homework> findAll();
	Iterable<Homework> findAllByFlow(long flow);
	Iterable<Homework> findAllByLessonDateAndFlow(LocalDate lessonDate, long flow);
	Iterable<Homework> findAllByFlowAndSubject(long flow, long subject);
	Optional<Homework> deleteById(long id);
	Optional<Homework> deleteByFlowAndLessonDateAndLessonNum(long flow, LocalDate lessonDate, int lessonNum);
	Iterable<Homework> deleteAll();
	Iterable<Homework> deleteAllBeforeDate(LocalDate date);
	Iterable<Homework> deleteAllByFlow(long flow);
	Iterable<Homework> deleteAllBySubject(long subject);
}
