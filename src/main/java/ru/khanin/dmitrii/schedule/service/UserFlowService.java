package ru.khanin.dmitrii.schedule.service;

import java.util.Collection;

import ru.khanin.dmitrii.schedule.entity.UserFlow;

public interface UserFlowService {
	UserFlow add(String login, int educationLevel, int course, int group, int subgroup);
	UserFlow findByUserAndFlow(String login, int educationLevel, int course, int group, int subgroup);
	Collection<UserFlow> findAllByUser(String login);
	Collection<UserFlow> findAllByFlow(int educationLevel, int course, int group, int subgroup);
	UserFlow deleteByUserAndFlow(String login, int educationLevel, int course, int group, int subgroup);
	boolean checkFlowAccess(String login, int educationLevel, int course, int group, int subgroup);
}
