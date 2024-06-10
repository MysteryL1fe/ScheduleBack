package ru.khanin.dmitrii.schedule.repo;

import java.util.Optional;

import ru.khanin.dmitrii.schedule.entity.Lesson;

public interface LessonRepo {
	Lesson add(Lesson lesson);
	Optional<Lesson> findById(long id);
	Optional<Lesson> findByNameAndTeacherAndCabinet(String name, String teacher, String cabinet);
	Iterable<Lesson> findAll();
	Optional<Lesson> deleteById(long id);
	Iterable<Lesson> deleteAll();
}
