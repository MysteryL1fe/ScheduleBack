package ru.khanin.dmitrii.schedule.service.jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.entity.Homework;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcFlowRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcHomeworkRepo;
import ru.khanin.dmitrii.schedule.service.HomeworkService;

@RequiredArgsConstructor
public class JdbcHomeworkService implements HomeworkService {
	private final JdbcHomeworkRepo homeworkRepo;
	private final JdbcFlowRepo flowRepo;

	@Override
	public Homework addOrUpdate(String homework, LocalDate lessonDate, int lessonNum, long flowId, String lessonName) {
		Homework homeworkToAdd = new Homework();
		homeworkToAdd.setHomework(homework);
		homeworkToAdd.setLessonDate(lessonDate);
		homeworkToAdd.setLessonNum(lessonNum);
		homeworkToAdd.setFlow(flowId);
		homeworkToAdd.setLessonName(lessonName);
		
		if (homeworkRepo.findByLessonDateAndLessonNumAndFlow(lessonDate, lessonNum, flowId).isPresent())
			return homeworkRepo.update(homeworkToAdd);
		
		return homeworkRepo.add(homeworkToAdd);
	}
	
	@Override
	@Transactional
	public Homework addOrUpdate(
			String homework, LocalDate lessonDate, int lessonNum, int flowLvl, int course, int flow,
			int subgroup, String lessonName
	) {
		Flow foundFlow = flowRepo
				.findByFlowLvlAndCourseAndFlowAndSubgroup(flowLvl, course, flow, subgroup)
				.orElseGet(() -> {
					Flow flowToAdd = new Flow();
					flowToAdd.setFlowLvl(flowLvl);
					flowToAdd.setCourse(course);
					flowToAdd.setFlow(flow);
					flowToAdd.setSubgroup(subgroup);
					Flow addedFlow = flowRepo.add(flowToAdd);
					
					return addedFlow;
				});
		
		return addOrUpdate(homework, lessonDate, lessonNum, foundFlow.getId(), lessonName);
	}

	@Override
	public Collection<Homework> findAll() {
		Iterable<? extends Homework> found = homeworkRepo.findAll();
		Collection<Homework> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}

	@Override
	public Collection<Homework> findAllByFlow(long flowId) {
		Iterable<Homework> found = homeworkRepo.findAllByFlow(flowId);
		Collection<Homework> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}
	
	@Override
	public Collection<Homework> findAllByFlow(int flowLvl, int course, int flow, int subgroup) {
		Flow foundFlow = flowRepo
				.findByFlowLvlAndCourseAndFlowAndSubgroup(flowLvl, course, flow, subgroup)
				.orElse(null);
		if (foundFlow == null) return null;
		
		return findAllByFlow(foundFlow.getId());
	}

	@Override
	public Collection<Homework> findAllByLessonDateAndFlow(LocalDate lessonDate, long flowId) {
		Iterable<Homework> found = homeworkRepo.findAllByLessonDateAndFlow(lessonDate, flowId);
		Collection<Homework> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}
	
	@Override
	public Homework delete(int flowLvl, int course, int flow, int subgroup, LocalDate lessonDate, int lessonNum) {
		Flow foundFlow = flowRepo
				.findByFlowLvlAndCourseAndFlowAndSubgroup(flowLvl, course, flow, subgroup)
				.orElse(null);
		if (foundFlow == null) return null;
		
		Homework foundHomework = homeworkRepo
				.findByLessonDateAndLessonNumAndFlow(lessonDate, lessonNum, foundFlow.getId())
				.orElse(null);
		if (foundHomework == null) return null;
		
		return deleteById(foundHomework.getId());
	}

	@Override
	public Homework deleteById(long id) {
		return homeworkRepo.deleteById(id).orElse(null);
	}

	@Override
	@Transactional
	public Collection<Homework> deleteAllBeforeDate(LocalDate date) {
		Iterable<Homework> deleted = homeworkRepo.deleteAllBeforeDate(date);
		Collection<Homework> result = new ArrayList<>();
		deleted.forEach(result::add);
		return result;
	}

	@Override
	@Transactional
	public Collection<Homework> deleteAll() {
		Iterable<Homework> deleted = homeworkRepo.deleteAll();
		Collection<Homework> result = new ArrayList<>();
		deleted.forEach(result::add);
		return result;
	}

}
