package ru.khanin.dmitrii.schedule.service;

import java.util.Collection;

import ru.khanin.dmitrii.schedule.entity.Teacher;

public interface TeacherService {
	Teacher add(String surname);
	Teacher add(String surname, String name, String patronymic);
	Teacher findById(long id);
	Teacher findBySurnameAndNameAndPatronymic(String surname, String name, String patronymic);
	Collection<Teacher> findAll();
	Collection<Teacher> findAllBySurname(String surname);
	Collection<Teacher> findAllWhereSurnameStartsWith(String surname);
	Collection<Teacher> findAllWhereFullnameStartsWith(String fullname);
	Teacher deleteById(long id);
	Teacher deleteBySurnameAndNameAndPatronymic(String surname, String name, String patronymic);
	Collection<Teacher> deleteAll();
}
