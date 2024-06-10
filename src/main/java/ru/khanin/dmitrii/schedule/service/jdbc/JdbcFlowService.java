package ru.khanin.dmitrii.schedule.service.jdbc;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcFlowRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcHomeworkRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcScheduleRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcTempScheduleRepo;
import ru.khanin.dmitrii.schedule.service.FlowService;

@RequiredArgsConstructor
public class JdbcFlowService implements FlowService {
	private final JdbcFlowRepo flowRepo;
	private final JdbcHomeworkRepo homeworkRepo;
	private final JdbcScheduleRepo scheduleRepo;
	private final JdbcTempScheduleRepo tempScheduleRepo;

	@Override
	public Flow add(int flowLvl, int course, int flow, int subgroup) {
		Flow flowToAdd = new Flow();
		flowToAdd.setFlowLvl(flowLvl);
		flowToAdd.setCourse(course);
		flowToAdd.setFlow(flow);
		flowToAdd.setSubgroup(subgroup);
		return flowRepo.add(flowToAdd);
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
		return flowRepo.deleteById(id).orElse(null);
	}

	@Override
	@Transactional
	public Collection<Flow> deleteAll() {
		homeworkRepo.deleteAll();
		scheduleRepo.deleteAll();
		tempScheduleRepo.deleteAll();
		Iterable<Flow> deleted = flowRepo.deleteAll();
		Collection<Flow> result = new ArrayList<>();
		deleted.forEach(result::add);
		return result;
	}

}
