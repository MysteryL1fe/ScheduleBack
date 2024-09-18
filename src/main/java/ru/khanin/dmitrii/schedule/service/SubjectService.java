package ru.khanin.dmitrii.schedule.service;

import java.util.Collection;

import ru.khanin.dmitrii.schedule.entity.Subject;

public interface SubjectService {
	Subject add(String subject);
	Subject findById(long id);
	Subject findBySubject(String subject);
	Collection<Subject> findAll();
	Collection<Subject> findAllWhereSubjectStartsWith(String subject);
	Subject deleteById(long id);
	Subject deleteBySubject(String subject);
	Collection<Subject> deleteAll();
}
