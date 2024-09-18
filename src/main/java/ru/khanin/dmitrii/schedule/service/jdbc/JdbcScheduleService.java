package ru.khanin.dmitrii.schedule.service.jdbc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.schedule.entity.Cabinet;
import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.entity.Schedule;
import ru.khanin.dmitrii.schedule.entity.Subject;
import ru.khanin.dmitrii.schedule.entity.Teacher;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcCabinetRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcFlowRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcScheduleRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcSubjectRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcTeacherRepo;
import ru.khanin.dmitrii.schedule.service.ScheduleService;

@RequiredArgsConstructor
public class JdbcScheduleService implements ScheduleService {
	private final JdbcScheduleRepo scheduleRepo;
	private final JdbcFlowRepo flowRepo;
	private final JdbcSubjectRepo subjectRepo;
	private final JdbcTeacherRepo teacherRepo;
	private final JdbcCabinetRepo cabinetRepo;

	@Override
	public Schedule findByFlowAndDayOfWeekAndLessonNumAndNumerator(
			long flowId, int dayOfWeek, int lessonNum, boolean numerator
	) {
		return scheduleRepo
				.findByFlowAndDayOfWeekAndLessonNumAndNumerator(
						flowId, dayOfWeek, lessonNum, numerator
				)
				.orElseThrow();
	}

	@Override
	public Collection<Schedule> findAll() {
		Iterable<? extends Schedule> found = scheduleRepo.findAll();
		Collection<Schedule> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}

	@Override
	public Collection<Schedule> findAllByFlow(long flowId) {
		Iterable<? extends Schedule> found = scheduleRepo.findAllByFlow(flowId);
		Collection<Schedule> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}
	
	@Override
	public Collection<Schedule> findAllByFlow(int educationLevel, int course, int group, int subgroup) {
		Flow foundFlow = flowRepo
				.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
				.orElseThrow();
		
		return findAllByFlow(foundFlow.getId());
	}

	@Override
	public Collection<Schedule> findAllByFlowAndDayOfWeekAndNumerator(
			long flowId, int dayOfWeek, boolean numerator
	) {
		Iterable<Schedule> found = scheduleRepo.findAllByFlowAndDayOfWeekAndNumerator(
				flowId, dayOfWeek, numerator
		);
		Collection<Schedule> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}

	@Override
	public Schedule deleteById(long id) {
		return scheduleRepo.deleteById(id).orElseThrow();
	}

	@Override
	@Transactional
	public Collection<Schedule> deleteAll() {
		Iterable<Schedule> deleted = scheduleRepo.deleteAll();
		Collection<Schedule> result = new ArrayList<>();
		deleted.forEach(result::add);
		return result;
	}

	@Override
	public Schedule addOrUpdate(long flowId, long subjectId, int dayOfWeek, int lessonNum, boolean numerator) {
		Schedule schedule = new Schedule();
		schedule.setFlow(flowId);
		schedule.setSubject(subjectId);
		schedule.setDayOfWeek(dayOfWeek);
		schedule.setLessonNum(lessonNum);
		schedule.setNumerator(numerator);
		
		Flow flow = flowRepo.findById(flowId).orElseThrow();
		flow.setLastEdit(LocalDateTime.now());
		flowRepo.update(flow);
		
		if (scheduleRepo
				.findByFlowAndDayOfWeekAndLessonNumAndNumerator(flowId, dayOfWeek, lessonNum, numerator)
				.isPresent())
			return scheduleRepo.update(schedule);
		
		return scheduleRepo.add(schedule);
	}

	@Override
	public Schedule addOrUpdate(
			int educationLevel, int course, int group, int subgroup, String subject,
			int dayOfWeek, int lessonNum, boolean numerator
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
				foundFlow.getId(), foundSubject.getId(), dayOfWeek, lessonNum, numerator
		);
	}

	@Override
	public Schedule addOrUpdate(
			long flowId, long subjectId, long teacherId, long cabinetId,
			int dayOfWeek, int lessonNum, boolean numerator
	) {
		Schedule schedule = new Schedule();
		schedule.setFlow(flowId);
		schedule.setSubject(subjectId);
		schedule.setTeacher(teacherId);
		schedule.setCabinet(cabinetId);
		schedule.setDayOfWeek(dayOfWeek);
		schedule.setLessonNum(lessonNum);
		schedule.setNumerator(numerator);
		
		Flow flow = flowRepo.findById(flowId).orElseThrow();
		flow.setLastEdit(LocalDateTime.now());
		flowRepo.update(flow);
		
		if (scheduleRepo
				.findByFlowAndDayOfWeekAndLessonNumAndNumerator(flowId, dayOfWeek, lessonNum, numerator)
				.isPresent())
			return scheduleRepo.update(schedule);
		
		return scheduleRepo.add(schedule);
	}

	@Override
	@Transactional
	public Schedule addOrUpdate(
			int educationLevel, int course, int group, int subgroup, String subject,
			String surname, String name, String patronymic, String cabinet, String building,
			int dayOfWeek, int lessonNum, boolean numerator
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
				dayOfWeek, lessonNum, numerator
		);
	}

	@Override
	public Schedule findById(long id) {
		return scheduleRepo.findById(id).orElseThrow();
	}

	@Override
	public Collection<Schedule> findAllByFlowAndDayOfWeekAndNumerator(
			int educationLevel, int course, int group, int subgroup, int dayOfWeek, boolean numerator
	) {
		Flow foundFlow = flowRepo
				.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
				.orElseThrow();
		
		return findAllByFlowAndDayOfWeekAndNumerator(foundFlow.getId(), dayOfWeek, numerator);
	}

	@Override
	public Collection<Schedule> findAllByTeacher(long teacherId) {
		Iterable<? extends Schedule> found = scheduleRepo.findAllByTeacher(teacherId);
		Collection<Schedule> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}

	@Override
	public Collection<Schedule> findAllByTeacher(String surname, String name, String patronymic) {
		Teacher foundTeacher = teacherRepo.findBySurnameAndNameAndPatronymic(surname, name, patronymic).orElseThrow();
		return findAllByTeacher(foundTeacher.getId());
	}
	
	@Override
	public Collection<Schedule> findAllWhereTeacherFullnameStartsWith(String fullname) {
		Iterable<Teacher> foundTeacher = teacherRepo.findAllWhereFullnameStartsWith(fullname);
		Collection<Schedule> result = new ArrayList<>();
		foundTeacher.forEach((e) -> result.addAll(findAllByTeacher(e.getId())));
		return result;
	}

	@Override
	public Schedule deleteByFlowAndDayOfWeekAndLessonNumAndNumerator(
			long flowId, int dayOfWeek, int lessonNum, boolean numerator
	) {
		Schedule foundSchedule = scheduleRepo
				.findByFlowAndDayOfWeekAndLessonNumAndNumerator(flowId, dayOfWeek, lessonNum, numerator)
				.orElseThrow();
		
		return deleteById(foundSchedule.getId());
	}

	@Override
	public Schedule deleteByFlowAndDayOfWeekAndLessonNumAndNumerator(
			int educationLevel, int course, int group, int subgroup, int dayOfWeek, int lessonNum, boolean numerator
	) {
		Flow foundFlow = flowRepo
				.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
				.orElseThrow();
		
		return deleteByFlowAndDayOfWeekAndLessonNumAndNumerator(
				foundFlow.getId(), dayOfWeek, lessonNum, numerator
		);
	}

	@Override
	public Schedule findByFlowAndDayOfWeekAndLessonNumAndNumerator(
			int educationLevel, int course, int group, int subgroup, int dayOfWeek, int lessonNum, boolean numerator
	) {
		Flow foundFlow = flowRepo
				.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
				.orElseThrow();
		
		return findByFlowAndDayOfWeekAndLessonNumAndNumerator(foundFlow.getId(), dayOfWeek, lessonNum, numerator);
	}

}
