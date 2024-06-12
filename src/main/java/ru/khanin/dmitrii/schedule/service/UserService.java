package ru.khanin.dmitrii.schedule.service;

import java.util.Collection;
import java.util.List;

import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.entity.User;
import ru.khanin.dmitrii.schedule.entity.User.AccessType;

public interface UserService {
	User add(String apiKey, String name, AccessType access);
	User add(String apiKey, String name, AccessType access, long flow);
	User add(String apiKey, String name, AccessType access, int flowLvl, int course, int flow, int subgroup);
	Collection<User> findAll();
	Collection<User> findAllByApiKey(String apiKey);
	Collection<User> deleteAll();
	Collection<User> deleteAllByApiKey(String apiKey);
	boolean checkAdminAccessByApiKey(String apiKey);
	boolean checkFlowAccessByApiKey(String apiKey, long flow);
	boolean checkFlowAccessByApiKey(String apiKey, int flowLvl, int course, int flow, int subgroup);
	boolean checkFlowsAccessByApiKey(String apiKey, List<Flow> flows);
}
