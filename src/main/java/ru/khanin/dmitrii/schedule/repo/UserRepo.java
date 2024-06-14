package ru.khanin.dmitrii.schedule.repo;

import java.util.Optional;

import ru.khanin.dmitrii.schedule.entity.User;

public interface UserRepo {
	User add(User user);
	Iterable<? extends User> findAll();
	Iterable<? extends User> findAllByApiKey(String apiKey);
	Optional<User> deleteById(long id);
	Iterable<? extends User> deleteAll();
	Iterable<? extends User> deleteAllWhereFlowIsNotNull();
	Iterable<? extends User> deleteAllByApiKey(String apiKey);
	Iterable<? extends User> deleteAllByFlow(long flow);
}
