package ru.khanin.dmitrii.schedule.service.jdbc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.schedule.entity.Cabinet;
import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.entity.Subject;
import ru.khanin.dmitrii.schedule.entity.Teacher;
import ru.khanin.dmitrii.schedule.entity.TempSchedule;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcCabinetRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcFlowRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcSubjectRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcTeacherRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcTempScheduleRepo;
import ru.khanin.dmitrii.schedule.service.TempScheduleService;

@RequiredArgsConstructor
public class JdbcTempScheduleService implements TempScheduleService {
	private final JdbcTempScheduleRepo tempScheduleRepo;
	private final JdbcFlowRepo flowRepo;
	private final JdbcSubjectRepo subjectRepo;
	private final JdbcTeacherRepo teacherRepo;
	private final JdbcCabinetRepo cabinetRepo;

	@Override
	public TempSchedule findByFlowAndLessonDateAndLessonNum(long flowId, LocalDate lessonDate, int lessonNum) {
		return tempScheduleRepo
				.findByFlowAndLessonDateAndLessonNum(flowId, lessonDate, lessonNum)
				.orElseThrow();
	}

	@Override
	public Collection<TempSchedule> findAll() {
		Iterable<? extends TempSchedule> found = tempScheduleRepo.findAll();
		Collection<TempSchedule> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}
	
	@Override
	public Collection<TempSchedule> findAllByFlow(long flowId) {
		Iterable<? extends TempSchedule> found = tempScheduleRepo.findAllByFlow(flowId);
		Collection<TempSchedule> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}
	
	@Override
	public Collection<TempSchedule> findAllByFlow(int educationLevel, int course, int group, int subgroup) {
		Flow foundFlow = flowRepo
				.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
				.orElseThrow();
		
		return findAllByFlow(foundFlow.getId());
	}

	@Override
	public Collection<TempSchedule> findAllByFlowAndLessonDate(long flowId, LocalDate lessonDate) {
		Iterable<TempSchedule> found = tempScheduleRepo.findAllByFlowAndLessonDate(flowId, lessonDate);
		Collection<TempSchedule> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}
	
	@Override
	public Collection<TempSchedule> findAllByFlowAndLessonDate(
			int educationLevel, int course, int group, int subgroup, LocalDate lessonDate
	) {
		Flow foundFlow = flowRepo
				.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
				.orElseThrow();
		
		return findAllByFlowAndLessonDate(foundFlow.getId(), lessonDate);
	}

	@Override
	public TempSchedule deleteById(long id) {
		return tempScheduleRepo.deleteById(id).orElseThrow();
	}

	@Override
	@Transactional
	public Collection<TempSchedule> deleteAll() {
		Iterable<TempSchedule> deleted = tempScheduleRepo.deleteAll();
		Collection<TempSchedule> result = new ArrayList<>();
		deleted.forEach(result::add);
		return result;
	}

	@Override
	@Transactional
	public Collection<TempSchedule> deleteAllBeforeDate(LocalDate date) {
		Iterable<TempSchedule> deleted = tempScheduleRepo.deleteAllBeforeDate(date);
		Collection<TempSchedule> result = new ArrayList<>();
		deleted.forEach(result::add);
		return result;
	}

	@Override
	public TempSchedule addOrUpdate(long flowId, LocalDate lessonDate, int lessonNum, boolean willLessonBe) {
		TempSchedule tempSchedule = new TempSchedule();
		tempSchedule.setFlow(flowId);
		tempSchedule.setLessonDate(lessonDate);
		tempSchedule.setLessonNum(lessonNum);
		tempSchedule.setWillLessonBe(willLessonBe);
		
		if (tempScheduleRepo
				.findByFlowAndLessonDateAndLessonNum(flowId, lessonDate, lessonNum)
				.isPresent())
			return tempScheduleRepo.update(tempSchedule);
		
		return tempScheduleRepo.add(tempSchedule);
	}

	@Override
	public TempSchedule addOrUpdate(
			int educationLevel, int course, int group, int subgroup,
			LocalDate lessonDate, int lessonNum, boolean willLessonBe
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
		
		return addOrUpdate(foundFlow.getId(), lessonDate, lessonNum, willLessonBe);
	}

	@Override
	public TempSchedule addOrUpdate(
			long flowId, long subjectId, LocalDate lessonDate, int lessonNum, boolean willLessonBe
	) {
		TempSchedule tempSchedule = new TempSchedule();
		tempSchedule.setFlow(flowId);
		tempSchedule.setSubject(subjectId);
		tempSchedule.setLessonDate(lessonDate);
		tempSchedule.setLessonNum(lessonNum);
		tempSchedule.setWillLessonBe(willLessonBe);
		
		if (tempScheduleRepo
				.findByFlowAndLessonDateAndLessonNum(flowId, lessonDate, lessonNum)
				.isPresent())
			return tempScheduleRepo.update(tempSchedule);
		
		return tempScheduleRepo.add(tempSchedule);
	}

	@Override
	public TempSchedule addOrUpdate(
			int educationLevel, int course, int group, int subgroup, String subject,
			LocalDate lessonDate, int lessonNum, boolean willLessonBe
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
		
		return addOrUpdate(
				foundFlow.getId(), foundSubject.getId(), lessonDate, lessonNum, willLessonBe
		);
	}

	@Override
	public TempSchedule addOrUpdate(
			long flowId, long subjectId, long teacherId, long cabinetId,
			LocalDate lessonDate, int lessonNum, boolean willLessonBe
	) {
		TempSchedule tempSchedule = new TempSchedule();
		tempSchedule.setFlow(flowId);
		tempSchedule.setSubject(subjectId);
		tempSchedule.setTeacher(teacherId);
		tempSchedule.setCabinet(cabinetId);
		tempSchedule.setLessonDate(lessonDate);
		tempSchedule.setLessonNum(lessonNum);
		tempSchedule.setWillLessonBe(willLessonBe);
		
		if (tempScheduleRepo
				.findByFlowAndLessonDateAndLessonNum(flowId, lessonDate, lessonNum)
				.isPresent())
			return tempScheduleRepo.update(tempSchedule);
		
		return tempScheduleRepo.add(tempSchedule);
	}

	@Override
	@Transactional
	public TempSchedule addOrUpdate(
			int educationLevel, int course, int group, int subgroup, String subject,
			String surname, String name, String patronymic, String cabinet, String building,
			LocalDate lessonDate, int lessonNum, boolean willLessonBe
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
		
		Teacher foundTeacher = teacherRepo
				.findBySurnameAndNameAndPatronymic(surname, name, patronymic)
				.orElseGet(() -> {
					Teacher teacherToAdd = new Teacher();
					teacherToAdd.setSurname(surname);
					teacherToAdd.setName(name);
					teacherToAdd.setPatronymic(patronymic);
					
					return teacherRepo.add(teacherToAdd);
				});
		
		Cabinet foundCabinet = cabinetRepo
				.findByCabinetAndBuilding(cabinet, building)
				.orElseGet(() -> {
					Cabinet cabinetToAdd = new Cabinet();
					cabinetToAdd.setCabinet(cabinet);
					cabinetToAdd.setBuilding(building);
					
					return cabinetRepo.add(cabinetToAdd);
				});
		
		return addOrUpdate(
				foundFlow.getId(), foundSubject.getId(), foundTeacher.getId(), foundCabinet.getId(),
				lessonDate, lessonNum, willLessonBe
		);
	}

	@Override
	public TempSchedule findById(long id) {
		return tempScheduleRepo.findById(id).orElseThrow();
	}

	@Override
	public TempSchedule findByFlowAndLessonDateAndLessonNum(
			int educationLevel, int course, int group, int subgroup, LocalDate lessonDate, int lessonNum
	) {
		Flow foundFlow = flowRepo
				.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
				.orElseThrow();
		
		return findByFlowAndLessonDateAndLessonNum(foundFlow.getId(), lessonDate, lessonNum);
	}

	@Override
	public TempSchedule deleteByFlowAndLessonDateAndLessonNum(long flowId, LocalDate lessonDate, int lessonNum) {
		TempSchedule foundSchedule = tempScheduleRepo
				.findByFlowAndLessonDateAndLessonNum(flowId, lessonDate, lessonNum)
				.orElseThrow();
		
		return deleteById(foundSchedule.getId());
	}

	@Override
	public TempSchedule deleteByFlowAndLessonDateAndLessonNum(
			int educationLevel, int course, int group, int subgroup, LocalDate lessonDate, int lessonNum
	) {
		Flow foundFlow = flowRepo
				.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
				.orElseThrow();
		
		return deleteByFlowAndLessonDateAndLessonNum(foundFlow.getId(), lessonDate, lessonNum);
	}

}
