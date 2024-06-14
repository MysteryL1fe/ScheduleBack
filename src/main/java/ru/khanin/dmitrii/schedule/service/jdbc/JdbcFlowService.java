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
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcUserRepo;
import ru.khanin.dmitrii.schedule.service.FlowService;

@RequiredArgsConstructor
public class JdbcFlowService implements FlowService {
	private final JdbcFlowRepo flowRepo;
	private final JdbcHomeworkRepo homeworkRepo;
	private final JdbcScheduleRepo scheduleRepo;
	private final JdbcTempScheduleRepo tempScheduleRepo;
	private final JdbcUserRepo userRepo;

	@Override
	public Flow addOrUpdate(int flowLvl, int course, int flow, int subgroup) {
		Flow flowToAdd = new Flow();
		flowToAdd.setFlowLvl(flowLvl);
		flowToAdd.setCourse(course);
		flowToAdd.setFlow(flow);
		flowToAdd.setSubgroup(subgroup);
		flowToAdd.setLastEdit(LocalDateTime.now());
		
		Optional<Flow> foundFlow = flowRepo.findByFlowLvlAndCourseAndFlowAndSubgroup(flowLvl, course, flow, subgroup);
		if (foundFlow.isPresent()) {
			flowToAdd.setLessonsStartDate(foundFlow.get().getLessonsStartDate());
			flowToAdd.setSessionStartDate(foundFlow.get().getSessionStartDate());
			flowToAdd.setSessionEndDate(foundFlow.get().getSessionEndDate());
			return flowRepo.update(flowToAdd);
		}
		
		return flowRepo.add(flowToAdd);
	}
	
	@Override
	public Flow addOrUpdate(int flowLvl, int course, int flow, int subgroup, LocalDate lessonsStartDate,
			LocalDate sessionStartDate, LocalDate sessionEndDate) {
		Flow flowToAdd = new Flow();
		flowToAdd.setFlowLvl(flowLvl);
		flowToAdd.setCourse(course);
		flowToAdd.setFlow(flow);
		flowToAdd.setSubgroup(subgroup);
		flowToAdd.setLastEdit(LocalDateTime.now());
		flowToAdd.setLessonsStartDate(lessonsStartDate);
		flowToAdd.setSessionStartDate(sessionStartDate);
		flowToAdd.setSessionEndDate(sessionEndDate);
		
		if (flowRepo.findByFlowLvlAndCourseAndFlowAndSubgroup(flowLvl, course, flow, subgroup).isPresent())
			return flowRepo.update(flowToAdd);
		
		return flowRepo.add(flowToAdd);
	}
	
	@Override
	public Flow findById(long id) {
		return flowRepo.findById(id).orElseThrow();
	}
	
	@Override
	public Flow findByFlowLvlAndCourseAndFlowAndSubgroup(int flowLvl, int course, int flow, int subgroup) {
		return flowRepo
				.findByFlowLvlAndCourseAndFlowAndSubgroup(flowLvl, course, flow, subgroup)
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
	public Collection<Flow> findAllByFlowLvl(int flowLvl) {
		Iterable<Flow> found = flowRepo.findAllByFlowLvl(flowLvl);
		Collection<Flow> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}

	@Override
	public Collection<Flow> findAllByFlowLvlAndCourse(int flowLvl, int course) {
		Iterable<Flow> found = flowRepo.findAllByFlowLvlAndCourse(flowLvl, course);
		Collection<Flow> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}

	@Override
	public Collection<Flow> findAllByFlowLvlAndCourseAndFlow(int flowLvl, int course, int flow) {
		Iterable<Flow> found = flowRepo.findAllByFlowLvlAndCourseAndFlow(flowLvl, course, flow);
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
		userRepo.deleteAllByFlow(id);
		return flowRepo.deleteById(id).orElseThrow();
	}

	@Override
	@Transactional
	public Collection<Flow> deleteAll() {
		homeworkRepo.deleteAll();
		scheduleRepo.deleteAll();
		tempScheduleRepo.deleteAll();
		userRepo.deleteAllWhereFlowIsNotNull();
		Iterable<Flow> deleted = flowRepo.deleteAll();
		Collection<Flow> result = new ArrayList<>();
		deleted.forEach(result::add);
		return result;
	}

}
