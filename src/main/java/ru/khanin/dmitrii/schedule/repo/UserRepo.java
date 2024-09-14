package ru.khanin.dmitrii.schedule.repo;

import java.util.Optional;

import ru.khanin.dmitrii.schedule.entity.User;

public interface UserRepo {
	User add(User user);
	User update(User user);
	Optional<User> findById(long id);
	Optional<User> findByLogin(String login);
	Iterable<User> findAll();
	Optional<User> deleteById(long id);
	Iterable<User> deleteAll();
}
