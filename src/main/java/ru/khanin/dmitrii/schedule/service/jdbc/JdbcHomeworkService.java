package ru.khanin.dmitrii.schedule.service.jdbc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.entity.Homework;
import ru.khanin.dmitrii.schedule.entity.Subject;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcFlowRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcHomeworkRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcSubjectRepo;
import ru.khanin.dmitrii.schedule.service.HomeworkService;

@RequiredArgsConstructor
public class JdbcHomeworkService implements HomeworkService {
	private final JdbcHomeworkRepo homeworkRepo;
	private final JdbcFlowRepo flowRepo;
	private final JdbcSubjectRepo subjectRepo;

	@Override
	public Homework addOrUpdate(String homework, LocalDate lessonDate, int lessonNum, long flowId, long subjectId) {
		Homework homeworkToAdd = new Homework();
		homeworkToAdd.setHomework(homework);
		homeworkToAdd.setLessonDate(lessonDate);
		homeworkToAdd.setLessonNum(lessonNum);
		homeworkToAdd.setFlow(flowId);
		homeworkToAdd.setSubject(subjectId);
		
		if (homeworkRepo.findByLessonDateAndLessonNumAndFlow(lessonDate, lessonNum, flowId).isPresent())
			return homeworkRepo.update(homeworkToAdd);
		
		return homeworkRepo.add(homeworkToAdd);
	}
	
	@Override
	@Transactional
	public Homework addOrUpdate(
			String homework, LocalDate lessonDate, int lessonNum,
			int educationLevel, int course, int group, int subgroup, String subject
	) {
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
		
		Subject foundSubject = subjectRepo
				.findBySubject(subject)
				.orElseGet(() -> {
					Subject subjectToAdd = new Subject();
					subjectToAdd.setSubject(subject);

					return subjectRepo.add(subjectToAdd);
				});
		
		return addOrUpdate(homework, lessonDate, lessonNum, foundFlow.getId(), foundSubject.getId());
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
	public Collection<Homework> findAllByFlow(int educationLevel, int course, int group, int subgroup) {
		Flow foundFlow = flowRepo
				.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
				.orElseThrow();
		
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
	public Homework deleteById(long id) {
		return homeworkRepo.deleteById(id).orElseThrow();
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

	@Override
	public Homework findById(long id) {
		return homeworkRepo.findById(id).orElseThrow();
	}

	@Override
	public Homework findByLessonDateAndLessonNumAndFlow(LocalDate lessonDate, int lessonNum, long flowId) {
		return homeworkRepo.findByLessonDateAndLessonNumAndFlow(lessonDate, lessonNum, flowId).orElseThrow();
	}

	@Override
	public Homework findByLessonDateAndLessonNumAndFlow(
			LocalDate lessonDate, int lessonNum, int educationLevel, int course, int group, int subgroup
	) {
		Flow foundFlow = flowRepo
				.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
				.orElseThrow();
		
		return findByLessonDateAndLessonNumAndFlow(lessonDate, lessonNum, foundFlow.getId());
	}

	@Override
	public Collection<Homework> findAllByLessonDateAndFlow(
			LocalDate lessonDate, int educationLevel, int course, int group, int subgroup
	) {
		Flow foundFlow = flowRepo
				.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
				.orElseThrow();
		
		return findAllByLessonDateAndFlow(lessonDate, foundFlow.getId());
	}

	@Override
	public Homework deleteByFlowAndLessonDateAndLessonNum(long flowId, LocalDate lessonDate, int lessonNum) {
		Homework foundHomework = homeworkRepo
				.findByLessonDateAndLessonNumAndFlow(lessonDate, lessonNum, flowId)
				.orElseThrow();
		if (foundHomework == null) return null;
		
		return deleteById(foundHomework.getId());
	}

	@Override
	public Homework deleteByFlowAndLessonDateAndLessonNum(
			int educationLevel, int course, int group, int subgroup, LocalDate lessonDate, int lessonNum
	) {
		Flow foundFlow = flowRepo
				.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
				.orElseThrow();
		
		return deleteByFlowAndLessonDateAndLessonNum(foundFlow.getId(), lessonDate, lessonNum);
	}

	@Override
	public Collection<Homework> findAllByFlowAndSubject(long flowId, long subjectId) {
		Iterable<Homework> found = homeworkRepo.findAllByFlowAndSubject(flowId, subjectId);
		Collection<Homework> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}

	@Override
	public Collection<Homework> findAllByFlowAndSubject(
			int educationLevel, int course, int group, int subgroup, String subject
	) {
		Flow foundFlow = flowRepo
				.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
				.orElseThrow();
		
		Subject foundSubject = subjectRepo
				.findBySubject(subject)
				.orElseThrow();
		
		return findAllByFlowAndSubject(foundFlow.getId(), foundSubject.getId());
	}

}
