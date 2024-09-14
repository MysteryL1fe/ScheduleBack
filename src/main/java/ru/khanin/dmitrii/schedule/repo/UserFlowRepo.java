package ru.khanin.dmitrii.schedule.repo;

import java.util.Optional;

import ru.khanin.dmitrii.schedule.entity.UserFlow;

public interface UserFlowRepo {
	UserFlow add(UserFlow userFlow);
	Optional<UserFlow> findByUserAndFlow(long user, long flow);
	Iterable<UserFlow> findAll();
	Iterable<UserFlow> findAllByUser(long user);
	Iterable<? extends UserFlow> findAllByFlow(long flow);
	Optional<UserFlow> deleteByUserAndFlow(long user, long flow);
	Iterable<UserFlow> deleteAllByUser(long user);
	Iterable<UserFlow> deleteAllByFlow(long flow);
	Iterable<UserFlow> deleteAll();
}
