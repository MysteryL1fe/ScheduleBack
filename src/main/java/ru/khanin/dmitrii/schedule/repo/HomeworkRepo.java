package ru.khanin.dmitrii.schedule.repo;

import java.time.LocalDate;
import java.util.Optional;

import ru.khanin.dmitrii.schedule.entity.Homework;

public interface HomeworkRepo {
	Homework add(Homework homework);
	Optional<Homework> findById(long id);
	Optional<Homework> findByLessonDateAndLessonNumAndFlow(LocalDate lessonDate, int lessonNum, long flow);
	Iterable<Homework> findAll();
	Iterable<Homework> findAllByFlow(long flow);
	Iterable<Homework> findAllByLessonDateAndFlow(LocalDate lessonDate, long flow);
	Optional<Homework> deleteById(long id);
	Iterable<Homework> deleteAll();
	Iterable<Homework> deleteAllBeforeDate(LocalDate date);
	Iterable<Homework> deleteAllByFlow(long flow);
}
