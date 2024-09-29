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

		UserFlow userFlow = new UserFlow();
		userFlow.setUser(foundUser);
		userFlow.setFlow(foundFlow);
		
		return userFlowRepo.add(userFlow);
	}

	@Override
	public UserFlow findByUserAndFlow(String login, int educationLevel, int course, int group, int subgroup) {
		User foundUser = userRepo.findByLogin(login).orElseThrow();
		
		Flow foundFlow = flowRepo
				.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
				.orElseThrow();

		return userFlowRepo.findByUserAndFlow(foundUser.getId(), foundFlow.getId()).orElseThrow();
	}

	@Override
	public Collection<UserFlow> findAllByUser(String login) {
		User foundUser = userRepo.findByLogin(login).orElseThrow();
		
		Iterable<UserFlow> found = userFlowRepo.findAllByUser(foundUser.getId());
		Collection<UserFlow> result = new ArrayList<>();
		found.forEach(result::add);
		
		return result;
	}

	@Override
	public Collection<UserFlow> findAllByFlow(int educationLevel, int course, int group, int subgroup) {
		Flow foundFlow = flowRepo
				.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
				.orElseThrow();

		Iterable<? extends UserFlow> found = userFlowRepo.findAllByFlow(foundFlow.getId());
		Collection<UserFlow> result = new ArrayList<>();
		found.forEach(result::add);
		
		return result;
	}

	@Override
	public UserFlow deleteByUserAndFlow(String login, int educationLevel, int course, int group, int subgroup) {
		User foundUser = userRepo.findByLogin(login).orElseThrow();
		Flow foundFlow = flowRepo
				.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
				.orElseThrow();

		return userFlowRepo.deleteByUserAndFlow(foundUser.getId(), foundFlow.getId()).orElseThrow();
	}

	@Override
	public boolean checkFlowAccess(String login, int educationLevel, int course, int group, int subgroup) {
		User foundUser = userRepo.findByLogin(login).orElseThrow();
		Flow foundFlow = flowRepo
				.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
				.orElseThrow();

		return userFlowRepo.findByUserAndFlow(foundUser.getId(), foundFlow.getId()).isPresent();
	}

}
