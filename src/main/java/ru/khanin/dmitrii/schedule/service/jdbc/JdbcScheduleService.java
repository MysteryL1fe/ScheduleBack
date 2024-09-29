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
	public Collection<Schedule> findAll() {
		Iterable<? extends Schedule> found = scheduleRepo.findAll();
		Collection<Schedule> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}
	
	@Override
	public Collection<Schedule> findAllByFlow(int educationLevel, int course, int group, int subgroup) {
		Flow foundFlow = flowRepo
				.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
				.orElseThrow();

		Iterable<? extends Schedule> found = scheduleRepo.findAllByFlow(foundFlow.getId());
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

		Schedule schedule = new Schedule();
		schedule.setFlow(foundFlow);
		schedule.setSubject(foundSubject);
		schedule.setDayOfWeek(dayOfWeek);
		schedule.setLessonNum(lessonNum);
		schedule.setNumerator(numerator);
		
		Flow flow = flowRepo.findById(foundFlow.getId()).orElseThrow();
		flow.setLastEdit(LocalDateTime.now());
		flowRepo.update(flow);
		
		if (scheduleRepo
				.findByFlowAndDayOfWeekAndLessonNumAndNumerator(foundFlow.getId(), dayOfWeek, lessonNum, numerator)
				.isPresent())
			return scheduleRepo.update(schedule);
		
		return scheduleRepo.add(schedule);
	}
	
	@Override
	public Schedule addOrUpdate(
			int educationLevel, int course, int group, int subgroup, String subject,
			String surname, String name, String patronymic, int dayOfWeek, int lessonNum, boolean numerator
	) {
		if (isTeacherEmpty(surname, name, patronymic)) {
			return addOrUpdate(
					educationLevel, course, group, subgroup, subject, dayOfWeek, lessonNum, numerator
			);
		}
		
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

		Schedule schedule = new Schedule();
		schedule.setFlow(foundFlow);
		schedule.setSubject(foundSubject);
		schedule.setTeacher(foundTeacher);
		schedule.setDayOfWeek(dayOfWeek);
		schedule.setLessonNum(lessonNum);
		schedule.setNumerator(numerator);
		
		Flow flow = flowRepo.findById(foundFlow.getId()).orElseThrow();
		flow.setLastEdit(LocalDateTime.now());
		flowRepo.update(flow);
		
		if (scheduleRepo
				.findByFlowAndDayOfWeekAndLessonNumAndNumerator(foundFlow.getId(), dayOfWeek, lessonNum, numerator)
				.isPresent())
			return scheduleRepo.update(schedule);
		
		return scheduleRepo.add(schedule);
	}
	
	@Override
	public Schedule addOrUpdate(
			int educationLevel, int course, int group, int subgroup, String subject,
			String cabinet, String building, int dayOfWeek, int lessonNum, boolean numerator
	) {
		if (isCabinetEmpty(cabinet, building)) {
			return addOrUpdate(
					educationLevel, course, group, subgroup, subject, dayOfWeek, lessonNum, numerator
			);
		}
		
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
		
		Cabinet foundCabinet = cabinetRepo
				.findByCabinetAndBuilding(cabinet, building)
				.orElseGet(() -> {
					Cabinet cabinetToAdd = new Cabinet();
					cabinetToAdd.setCabinet(cabinet);
					cabinetToAdd.setBuilding(building);
					
					return cabinetRepo.add(cabinetToAdd);
				});

		Schedule schedule = new Schedule();
		schedule.setFlow(foundFlow);
		schedule.setSubject(foundSubject);
		schedule.setCabinet(foundCabinet);
		schedule.setDayOfWeek(dayOfWeek);
		schedule.setLessonNum(lessonNum);
		schedule.setNumerator(numerator);
		
		Flow flow = flowRepo.findById(foundFlow.getId()).orElseThrow();
		flow.setLastEdit(LocalDateTime.now());
		flowRepo.update(flow);
		
		if (scheduleRepo
				.findByFlowAndDayOfWeekAndLessonNumAndNumerator(foundFlow.getId(), dayOfWeek, lessonNum, numerator)
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
		if (isTeacherEmpty(surname, name, patronymic) && isCabinetEmpty(cabinet, building)) {
			return addOrUpdate(
					educationLevel, course, group, subgroup, subject, dayOfWeek, lessonNum, numerator
			);
		} else if (isTeacherEmpty(surname, name, patronymic)) {
			return addOrUpdate(
					educationLevel, course, group, subgroup, subject, cabinet, building,
					dayOfWeek, lessonNum, numerator
			);
		} else if (isCabinetEmpty(cabinet, building)) {
			return addOrUpdate(
					educationLevel, course, group, subgroup, subject, surname, name, patronymic,
					dayOfWeek, lessonNum, numerator
			);
		}
		
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

		Schedule schedule = new Schedule();
		schedule.setFlow(foundFlow);
		schedule.setSubject(foundSubject);
		schedule.setTeacher(foundTeacher);
		schedule.setCabinet(foundCabinet);
		schedule.setDayOfWeek(dayOfWeek);
		schedule.setLessonNum(lessonNum);
		schedule.setNumerator(numerator);
		
		Flow flow = flowRepo.findById(foundFlow.getId()).orElseThrow();
		flow.setLastEdit(LocalDateTime.now());
		flowRepo.update(flow);
		
		if (scheduleRepo
				.findByFlowAndDayOfWeekAndLessonNumAndNumerator(foundFlow.getId(), dayOfWeek, lessonNum, numerator)
				.isPresent())
			return scheduleRepo.update(schedule);
		
		return scheduleRepo.add(schedule);
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

		Iterable<Schedule> found = scheduleRepo.findAllByFlowAndDayOfWeekAndNumerator(
				foundFlow.getId(), dayOfWeek, numerator
		);
		Collection<Schedule> result = new ArrayList<>();
		found.forEach(result::add);
		
		return result;
	}

	@Override
	public Collection<Schedule> findAllByTeacher(String surname, String name, String patronymic) {
		Teacher foundTeacher = teacherRepo
				.findBySurnameAndNameAndPatronymic(surname, name, patronymic)
				.orElseThrow();
		
		Iterable<? extends Schedule> found = scheduleRepo.findAllByTeacher(foundTeacher.getId());
		Collection<Schedule> result = new ArrayList<>();
		found.forEach(result::add);
		
		return result;
	}

	@Override
	public Schedule deleteByFlowAndDayOfWeekAndLessonNumAndNumerator(
			int educationLevel, int course, int group, int subgroup, int dayOfWeek, int lessonNum, boolean numerator
	) {
		Flow foundFlow = flowRepo
				.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
				.orElseThrow();

		Schedule foundSchedule = scheduleRepo
				.findByFlowAndDayOfWeekAndLessonNumAndNumerator(foundFlow.getId(), dayOfWeek, lessonNum, numerator)
				.orElseThrow();
		
		return deleteById(foundSchedule.getId());
	}

	@Override
	public Schedule findByFlowAndDayOfWeekAndLessonNumAndNumerator(
			int educationLevel, int course, int group, int subgroup, int dayOfWeek, int lessonNum, boolean numerator
	) {
		Flow foundFlow = flowRepo
				.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
				.orElseThrow();

		return scheduleRepo
				.findByFlowAndDayOfWeekAndLessonNumAndNumerator(
						foundFlow.getId(), dayOfWeek, lessonNum, numerator
				)
				.orElseThrow();
	}
	
	private boolean isTeacherEmpty(String surname, String name, String patronymic) {
		return (surname == null || surname.isBlank()) && (name == null || name.isBlank())
				&& (patronymic == null || patronymic.isBlank());
	}
	
	private boolean isCabinetEmpty(String cabinet, String building) {
		return (cabinet == null || cabinet.isBlank()) && (building == null || building.isBlank());
	}

}
