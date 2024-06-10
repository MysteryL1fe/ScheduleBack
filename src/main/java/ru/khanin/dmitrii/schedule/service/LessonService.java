package ru.khanin.dmitrii.schedule.service;

import java.util.Collection;

import ru.khanin.dmitrii.schedule.entity.Lesson;

public interface LessonService {
	Lesson add(String name, String teacher, String cabinet);
	Lesson findById(long id);
	Collection<Lesson> findAll();
	Lesson deleteById(long id);
	Collection<Lesson> deleteAll();
}
