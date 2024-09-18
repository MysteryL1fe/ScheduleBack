package ru.khanin.dmitrii.schedule.service;

import java.util.Collection;

import ru.khanin.dmitrii.schedule.entity.User;

public interface UserService {
	User addOrUpdate(String login, String password, boolean admin);
	User findById(long id);
	User findByLogin(String login);
	Collection<User> findAll();
	User deleteById(long id);
	User deleteByLogin(String login);
	boolean isAdmin(String login);
}
