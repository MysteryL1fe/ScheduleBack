package ru.khanin.dmitrii.schedule.service.jdbc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.entity.User;
import ru.khanin.dmitrii.schedule.entity.User.AccessType;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcFlowRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcUserRepo;
import ru.khanin.dmitrii.schedule.service.UserService;

@RequiredArgsConstructor
public class JdbcUserService implements UserService {
	private final JdbcUserRepo userRepo;
	private final JdbcFlowRepo flowRepo;

	@Override
	public User add(String apiKey, String name, AccessType access) {
		if (access == AccessType.flow) return null;
		
		User userToAdd = new User();
		userToAdd.setApiKey(apiKey);
		userToAdd.setName(name);
		userToAdd.setAccess(access);
		return userRepo.add(userToAdd);
	}
	
	@Override
	public User add(String apiKey, String name, AccessType access, long flow) {
		if (access == AccessType.admin) return add(apiKey, name, access);
		
		User userToAdd = new User();
		userToAdd.setApiKey(apiKey);
		userToAdd.setName(name);
		userToAdd.setAccess(access);
		return userRepo.add(userToAdd);
	}
	
	@Override
	@Transactional
	public User add(String apiKey, String name, AccessType access, int flowLvl, int course, int flow, int subgroup) {
		if (access == AccessType.admin) return add(apiKey, name, access);
		
		Flow foundFlow = flowRepo
				.findByFlowLvlAndCourseAndFlowAndSubgroup(flowLvl, course, flow, subgroup)
				.orElse(null);
		if (foundFlow == null) {
			Flow flowToAdd = new Flow();
			flowToAdd.setFlowLvl(flowLvl);
			flowToAdd.setCourse(course);
			flowToAdd.setFlow(flow);
			flowToAdd.setSubgroup(subgroup);
			Flow addedFlow = flowRepo.add(flowToAdd);
			
			return add(apiKey, name, access, addedFlow.getId());
		}
		
		return add(apiKey, name, access, foundFlow.getId());
	}

	@Override
	public Collection<User> findAll() {
		Iterable<? extends User> found = userRepo.findAll();
		Collection<User> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}

	@Override
	public Collection<User> findAllByApiKey(String apiKey) {
		Iterable<? extends User> found = userRepo.findAllByApiKey(apiKey);
		Collection<User> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}

	@Override
	@Transactional
	public Collection<User> deleteAll() {
		Iterable<? extends User> found = userRepo.deleteAll();
		Collection<User> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}

	@Override
	@Transactional
	public Collection<User> deleteAllByApiKey(String apiKey) {
		Iterable<? extends User> found = userRepo.deleteAllByApiKey(apiKey);
		Collection<User> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}

	@Override
	public boolean checkAdminAccessByApiKey(String apiKey) {
		Iterable<? extends User> users = userRepo.findAllByApiKey(apiKey);
		for (User user : users) {
			if (user.getAccess() == AccessType.admin) return true;
		}
		return false;
	}
	
	@Override
	public boolean checkFlowAccessByApiKey(String apiKey, long flow) {
		Iterable<? extends User> users = userRepo.findAllByApiKey(apiKey);
		for (User user : users) {
			if (user.getAccess() == AccessType.admin
					|| user.getAccess() == AccessType.flow && user.getFlow() == flow)
				return true;
		}
		return false;
	}
	
	@Override
	public boolean checkFlowAccessByApiKey(String apiKey, int flowLvl, int course, int flow, int subgroup) {
		Optional<Flow> flowToAccess = flowRepo
				.findByFlowLvlAndCourseAndFlowAndSubgroup(flowLvl, course, flow, subgroup);
		return flowToAccess.isPresent() && checkFlowAccessByApiKey(apiKey, flowToAccess.get().getId());
	}
	
	@Override
	public boolean checkFlowsAccessByApiKey(String apiKey, List<Flow> flows) {
		List<Long> flowsToAccess = new ArrayList<>();
		for (Flow flow : flows) {
			Optional<Flow> flowToAccess = flowRepo
					.findByFlowLvlAndCourseAndFlowAndSubgroup(
							flow.getFlowLvl(), flow.getCourse(), flow.getFlow(), flow.getSubgroup()
					);
			if (flowToAccess.isEmpty()) return false;
			flowsToAccess.add(flowToAccess.get().getId());
		};
		
		Iterable<? extends User> users = userRepo.findAllByApiKey(apiKey);
		List<Long> accessedFlows = new ArrayList<>();
		for (User user : users) {
			if (user.getAccess() == AccessType.admin) return true;
			if (user.getAccess() == AccessType.flow) accessedFlows.add(user.getFlow());
		}
		
		if (accessedFlows.containsAll(flowsToAccess)) return true;
		
		return false;
	}
}
