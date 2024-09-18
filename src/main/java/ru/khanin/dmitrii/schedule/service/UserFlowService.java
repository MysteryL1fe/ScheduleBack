package ru.khanin.dmitrii.schedule.service;

import java.util.Collection;

import ru.khanin.dmitrii.schedule.entity.UserFlow;

public interface UserFlowService {
	UserFlow add(long userId, long flowId);
	UserFlow add(String login, int educationLevel, int course, int group, int subgroup);
	UserFlow findByUserAndFlow(long userId, long flowId);
	UserFlow findByUserAndFlow(String login, int educationLevel, int course, int group, int subgroup);
	Collection<UserFlow> findAllByUser(long userId);
	Collection<UserFlow> findAllByUser(String login);
	Collection<UserFlow> findAllByFlow(long flowId);
	Collection<UserFlow> findAllByFlow(int educationLevel, int course, int group, int subgroup);
	UserFlow deleteByUserAndFlow(long userId, long flowId);
	UserFlow deleteByUserAndFlow(String login, int educationLevel, int course, int group, int subgroup);
	boolean checkFlowAccess(long userId, long flowId);
	boolean checkFlowAccess(String login, int educationLevel, int course, int group, int subgroup);
}
