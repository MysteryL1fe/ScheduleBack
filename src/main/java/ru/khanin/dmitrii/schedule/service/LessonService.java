package ru.khanin.dmitrii.schedule.service;

import java.util.Collection;

import ru.khanin.dmitrii.schedule.entity.Lesson;

public interface LessonService {
	Lesson add(String name, String teacher, String cabinet);
	Collection<Lesson> findAll();
	Collection<Lesson> findAllByTeacher(String teacher);
	Lesson deleteById(long id);
	Collection<Lesson> deleteAll();
}
