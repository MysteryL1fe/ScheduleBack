package ru.khanin.dmitrii.schedule.service.jdbc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcFlowRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcHomeworkRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcScheduleRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcTempScheduleRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcUserFlowRepo;
import ru.khanin.dmitrii.schedule.service.FlowService;

@RequiredArgsConstructor
public class JdbcFlowService implements FlowService {
	private final JdbcFlowRepo flowRepo;
	private final JdbcHomeworkRepo homeworkRepo;
	private final JdbcScheduleRepo scheduleRepo;
	private final JdbcTempScheduleRepo tempScheduleRepo;
	private final JdbcUserFlowRepo userFlowRepo;

	@Override
	public Flow addOrUpdate(int educationLevel, int course, int group, int subgroup) {
		Flow flowToAdd = new Flow();
		flowToAdd.setEducationLevel(educationLevel);
		flowToAdd.setCourse(course);
		flowToAdd.setGroup(group);
		flowToAdd.setSubgroup(subgroup);
		flowToAdd.setLastEdit(LocalDateTime.now());
		flowToAdd.setActive(true);
		
		Optional<Flow> foundFlow = flowRepo.
				findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup);
		
		if (foundFlow.isPresent()) {
			flowToAdd.setLessonsStartDate(foundFlow.get().getLessonsStartDate());
			flowToAdd.setSessionStartDate(foundFlow.get().getSessionStartDate());
			flowToAdd.setSessionEndDate(foundFlow.get().getSessionEndDate());
			flowToAdd.setActive(foundFlow.get().isActive());
			return flowRepo.update(flowToAdd);
		}
		
		return flowRepo.add(flowToAdd);
	}
	
	@Override
	public Flow addOrUpdate(
			int educationLevel, int course, int group, int subgroup,
			LocalDate lessonsStartDate, LocalDate sessionStartDate, LocalDate sessionEndDate
		) {
		Flow flowToAdd = new Flow();
		flowToAdd.setEducationLevel(educationLevel);
		flowToAdd.setCourse(course);
		flowToAdd.setGroup(group);
		flowToAdd.setSubgroup(subgroup);
		flowToAdd.setLastEdit(LocalDateTime.now());
		flowToAdd.setLessonsStartDate(lessonsStartDate);
		flowToAdd.setSessionStartDate(sessionStartDate);
		flowToAdd.setSessionEndDate(sessionEndDate);
		flowToAdd.setActive(true);
		
		Optional<Flow> foundFlow = flowRepo.
				findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup);
		
		if (foundFlow.isPresent()) {
			flowToAdd.setActive(foundFlow.get().isActive());
			return flowRepo.update(flowToAdd);
		}
		
		return flowRepo.add(flowToAdd);
	}
	
	@Override
	public Flow addOrUpdate(int educationLevel, int course, int group, int subgroup, boolean active) {
		Flow flowToAdd = new Flow();
		flowToAdd.setEducationLevel(educationLevel);
		flowToAdd.setCourse(course);
		flowToAdd.setGroup(group);
		flowToAdd.setSubgroup(subgroup);
		flowToAdd.setLastEdit(LocalDateTime.now());
		flowToAdd.setActive(active);
		
		Optional<Flow> foundFlow = flowRepo.
				findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup);
		
		if (foundFlow.isPresent()) {
			flowToAdd.setLessonsStartDate(foundFlow.get().getLessonsStartDate());
			flowToAdd.setSessionStartDate(foundFlow.get().getSessionStartDate());
			flowToAdd.setSessionEndDate(foundFlow.get().getSessionEndDate());
			return flowRepo.update(flowToAdd);
		}
		
		return flowRepo.add(flowToAdd);
	}
	
	@Override
	public Flow addOrUpdate(
			int educationLevel, int course, int group, int subgroup, LocalDate lessonsStartDate,
			LocalDate sessionStartDate, LocalDate sessionEndDate, boolean active
	) {
		Flow flowToAdd = new Flow();
		flowToAdd.setEducationLevel(educationLevel);
		flowToAdd.setCourse(course);
		flowToAdd.setGroup(group);
		flowToAdd.setSubgroup(subgroup);
		flowToAdd.setLastEdit(LocalDateTime.now());
		flowToAdd.setLessonsStartDate(lessonsStartDate);
		flowToAdd.setSessionStartDate(sessionStartDate);
		flowToAdd.setSessionEndDate(sessionEndDate);
		flowToAdd.setActive(active);
		
		if (flowRepo
				.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
				.isPresent()) {
			return flowRepo.update(flowToAdd);
		}
		
		return flowRepo.add(flowToAdd);
	}
	
	@Override
	public Flow findById(long id) {
		return flowRepo.findById(id).orElseThrow();
	}
	
	@Override
	public Flow findByEducationLevelAndCourseAndGroupAndSubgroup(int educationLevel, int course, int group, int subgroup) {
		return flowRepo
				.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
				.orElseThrow();
	}

	@Override
	public Collection<Flow> findAll() {
		Iterable<Flow> found = flowRepo.findAll();
		Collection<Flow> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}
	
	@Override
	public Collection<Flow> findAllActive() {
		Iterable<Flow> found = flowRepo.findAllActive();
		Collection<Flow> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}

	@Override
	public Collection<Flow> findAllByEducationLevel(int educationLevel) {
		Iterable<Flow> found = flowRepo.findAllByEducationLevel(educationLevel);
		Collection<Flow> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}

	@Override
	public Collection<Flow> findAllByEducationLevelAndCourse(int educationLevel, int course) {
		Iterable<Flow> found = flowRepo.findAllByEducationLevelAndCourse(educationLevel, course);
		Collection<Flow> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}

	@Override
	public Collection<Flow> findAllByEducationLevelAndCourseAndGroup(int educationLevel, int course, int group) {
		Iterable<Flow> found = flowRepo.findAllByEducationLevelAndCourseAndGroup(educationLevel, course, group);
		Collection<Flow> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}

	@Override
	@Transactional
	public Flow deleteById(long id) {
		homeworkRepo.deleteAllByFlow(id);
		scheduleRepo.deleteAllByFlow(id);
		tempScheduleRepo.deleteAllByFlow(id);
		userFlowRepo.deleteAllByFlow(id);
		return flowRepo.deleteById(id).orElseThrow();
	}

	@Override
	@Transactional
	public Collection<Flow> deleteAll() {
		homeworkRepo.deleteAll();
		scheduleRepo.deleteAll();
		tempScheduleRepo.deleteAll();
		userFlowRepo.deleteAll();
		Iterable<Flow> deleted = flowRepo.deleteAll();
		Collection<Flow> result = new ArrayList<>();
		deleted.forEach(result::add);
		return result;
	}

}
