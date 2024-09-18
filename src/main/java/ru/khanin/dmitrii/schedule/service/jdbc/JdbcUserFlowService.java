package ru.khanin.dmitrii.schedule.service.jdbc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.entity.User;
import ru.khanin.dmitrii.schedule.entity.UserFlow;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcFlowRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcUserFlowRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcUserRepo;
import ru.khanin.dmitrii.schedule.service.UserFlowService;

@RequiredArgsConstructor
public class JdbcUserFlowService implements UserFlowService {
	private final JdbcUserFlowRepo userFlowRepo;
	private final JdbcUserRepo userRepo;
	private final JdbcFlowRepo flowRepo;

	@Override
	public UserFlow add(long userId, long flowId) {
		UserFlow userFlow = new UserFlow();
		userFlow.setUser(userId);
		userFlow.setFlow(flowId);
		return userFlowRepo.add(userFlow);
	}

	@Override
	@Transactional
	public UserFlow add(String login, int educationLevel, int course, int group, int subgroup) {
		User foundUser = userRepo
				.findByLogin(login)
				.orElseGet(() -> {
					User userToAdd = new User();
					userToAdd.setLogin(login);
					
					return userRepo.add(userToAdd);
				});
		
		Flow foundFlow = flowRepo
				.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
				.orElseGet(() -> {
					Flow flowToAdd = new Flow();
					flowToAdd.setEducationLevel(educationLevel);
					flowToAdd.setCourse(course);
					flowToAdd.setGroup(group);
					flowToAdd.setSubgroup(subgroup);
					flowToAdd.setLastEdit(LocalDateTime.now());
					
					return flowRepo.add(flowToAdd);
				});
		
		return add(foundUser.getId(), foundFlow.getId());
	}

	@Override
	public UserFlow findByUserAndFlow(long userId, long flowId) {
		return userFlowRepo.findByUserAndFlow(userId, flowId).orElseThrow();
	}

	@Override
	public UserFlow findByUserAndFlow(String login, int educationLevel, int course, int group, int subgroup) {
		User foundUser = userRepo.findByLogin(login).orElseThrow();
		Flow foundFlow = flowRepo
				.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
				.orElseThrow();
		
		return findByUserAndFlow(foundUser.getId(), foundFlow.getId());
	}

	@Override
	public Collection<UserFlow> findAllByUser(long userId) {
		Iterable<UserFlow> found = userFlowRepo.findAllByUser(userId);
		Collection<UserFlow> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}

	@Override
	public Collection<UserFlow> findAllByUser(String login) {
		User foundUser = userRepo.findByLogin(login).orElseThrow();
		return findAllByUser(foundUser.getId());
	}

	@Override
	public Collection<UserFlow> findAllByFlow(long flowId) {
		Iterable<? extends UserFlow> found = userFlowRepo.findAllByFlow(flowId);
		Collection<UserFlow> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}

	@Override
	public Collection<UserFlow> findAllByFlow(int educationLevel, int course, int group, int subgroup) {
		Flow foundFlow = flowRepo
				.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
				.orElseThrow();
		
		return findAllByFlow(foundFlow.getId());
	}

	@Override
	public UserFlow deleteByUserAndFlow(long userId, long flowId) {
		return userFlowRepo.deleteByUserAndFlow(userId, flowId).orElseThrow();
	}

	@Override
	public UserFlow deleteByUserAndFlow(String login, int educationLevel, int course, int group, int subgroup) {
		User foundUser = userRepo.findByLogin(login).orElseThrow();
		Flow foundFlow = flowRepo
				.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
				.orElseThrow();
		
		return deleteByUserAndFlow(foundUser.getId(), foundFlow.getId());
	}

	@Override
	public boolean checkFlowAccess(long userId, long flowId) {
		return userFlowRepo.findByUserAndFlow(userId, flowId).isPresent();
	}

	@Override
	public boolean checkFlowAccess(String login, int educationLevel, int course, int group, int subgroup) {
		User foundUser = userRepo.findByLogin(login).orElseThrow();
		Flow foundFlow = flowRepo
				.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
				.orElseThrow();
		
		return checkFlowAccess(foundUser.getId(), foundFlow.getId());
	}

}
