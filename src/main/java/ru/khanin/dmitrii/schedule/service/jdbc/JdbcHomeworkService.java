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
		
		Homework homeworkToAdd = new Homework();
		homeworkToAdd.setHomework(homework);
		homeworkToAdd.setLessonDate(lessonDate);
		homeworkToAdd.setLessonNum(lessonNum);
		homeworkToAdd.setFlow(foundFlow.getId());
		homeworkToAdd.setSubject(foundSubject.getId());
		
		if (homeworkRepo.findByLessonDateAndLessonNumAndFlow(lessonDate, lessonNum, foundFlow.getId()).isPresent())
			return homeworkRepo.update(homeworkToAdd);
		
		return homeworkRepo.add(homeworkToAdd);
	}

	@Override
	public Collection<Homework> findAll() {
		Iterable<? extends Homework> found = homeworkRepo.findAll();
		Collection<Homework> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}
	
	@Override
	public Collection<Homework> findAllByFlow(int educationLevel, int course, int group, int subgroup) {
		Flow foundFlow = flowRepo
				.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
				.orElseThrow();
		
		Iterable<Homework> found = homeworkRepo.findAllByFlow(foundFlow.getId());
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
	public Homework findByLessonDateAndLessonNumAndFlow(
			LocalDate lessonDate, int lessonNum, int educationLevel, int course, int group, int subgroup
	) {
		Flow foundFlow = flowRepo
				.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
				.orElseThrow();

		return homeworkRepo
				.findByLessonDateAndLessonNumAndFlow(lessonDate, lessonNum, foundFlow.getId())
				.orElseThrow();
	}

	@Override
	public Collection<Homework> findAllByLessonDateAndFlow(
			LocalDate lessonDate, int educationLevel, int course, int group, int subgroup
	) {
		Flow foundFlow = flowRepo
				.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
				.orElseThrow();

		Iterable<Homework> found = homeworkRepo.findAllByLessonDateAndFlow(lessonDate, foundFlow.getId());
		Collection<Homework> result = new ArrayList<>();
		found.forEach(result::add);
		
		return result;
	}

	@Override
	public Homework deleteByFlowAndLessonDateAndLessonNum(
			int educationLevel, int course, int group, int subgroup, LocalDate lessonDate, int lessonNum
	) {
		Flow foundFlow = flowRepo
				.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
				.orElseThrow();
		
		Homework foundHomework = homeworkRepo
				.findByLessonDateAndLessonNumAndFlow(lessonDate, lessonNum, foundFlow.getId())
				.orElseThrow();
		
		return deleteById(foundHomework.getId());
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

		Iterable<Homework> found = homeworkRepo.findAllByFlowAndSubject(foundFlow.getId(), foundSubject.getId());
		Collection<Homework> result = new ArrayList<>();
		found.forEach(result::add);
		
		return result;
	}

}
