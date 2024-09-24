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
	public Collection<TempSchedule> findAll() {
		Iterable<? extends TempSchedule> found = tempScheduleRepo.findAll();
		Collection<TempSchedule> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}
	
	@Override
	public Collection<TempSchedule> findAllByFlow(int educationLevel, int course, int group, int subgroup) {
		Flow foundFlow = flowRepo
				.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
				.orElseThrow();

		Iterable<? extends TempSchedule> found = tempScheduleRepo.findAllByFlow(foundFlow.getId());
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

		Iterable<TempSchedule> found = tempScheduleRepo.findAllByFlowAndLessonDate(foundFlow.getId(), lessonDate);
		Collection<TempSchedule> result = new ArrayList<>();
		found.forEach(result::add);
		
		return result;
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

		TempSchedule tempSchedule = new TempSchedule();
		tempSchedule.setFlow(foundFlow.getId());
		tempSchedule.setLessonDate(lessonDate);
		tempSchedule.setLessonNum(lessonNum);
		tempSchedule.setWillLessonBe(willLessonBe);
		
		if (tempScheduleRepo
				.findByFlowAndLessonDateAndLessonNum(foundFlow.getId(), lessonDate, lessonNum)
				.isPresent())
			return tempScheduleRepo.update(tempSchedule);
		
		return tempScheduleRepo.add(tempSchedule);
	}

	@Override
	public TempSchedule addOrUpdate(
			int educationLevel, int course, int group, int subgroup, String subject,
			LocalDate lessonDate, int lessonNum, boolean willLessonBe
	) {
		if (isSubjectEmpty(subject)) {
			return addOrUpdate(educationLevel, course, group, subgroup, lessonDate, lessonNum, willLessonBe);
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

		TempSchedule tempSchedule = new TempSchedule();
		tempSchedule.setFlow(foundFlow.getId());
		tempSchedule.setSubject(foundSubject.getId());
		tempSchedule.setLessonDate(lessonDate);
		tempSchedule.setLessonNum(lessonNum);
		tempSchedule.setWillLessonBe(willLessonBe);
		
		if (tempScheduleRepo
				.findByFlowAndLessonDateAndLessonNum(foundFlow.getId(), lessonDate, lessonNum)
				.isPresent())
			return tempScheduleRepo.update(tempSchedule);
		
		return tempScheduleRepo.add(tempSchedule);
	}
	
	@Override
	public TempSchedule addOrUpdate(
			int educationLevel, int course, int group, int subgroup, String subject,
			String surname, String name, String patronymic, LocalDate lessonDate, int lessonNum, boolean willLessonBe
	) {
		if (isSubjectEmpty(subject)) {
			return addOrUpdate(educationLevel, course, group, subgroup, lessonDate, lessonNum, willLessonBe);
		} else if (isTeacherEmpty(surname, name, patronymic)) {
			return addOrUpdate(
					educationLevel, course, group, subgroup, subject, lessonDate, lessonNum, willLessonBe
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

		TempSchedule tempSchedule = new TempSchedule();
		tempSchedule.setFlow(foundFlow.getId());
		tempSchedule.setSubject(foundSubject.getId());
		tempSchedule.setTeacher(foundTeacher.getId());
		tempSchedule.setLessonDate(lessonDate);
		tempSchedule.setLessonNum(lessonNum);
		tempSchedule.setWillLessonBe(willLessonBe);
		
		if (tempScheduleRepo
				.findByFlowAndLessonDateAndLessonNum(foundFlow.getId(), lessonDate, lessonNum)
				.isPresent())
			return tempScheduleRepo.update(tempSchedule);
		
		return tempScheduleRepo.add(tempSchedule);
	}
	
	@Override
	public TempSchedule addOrUpdate(
			int educationLevel, int course, int group, int subgroup, String subject,
			String cabinet, String building, LocalDate lessonDate, int lessonNum, boolean willLessonBe
	) {
		if (isSubjectEmpty(subject)) {
			return addOrUpdate(educationLevel, course, group, subgroup, lessonDate, lessonNum, willLessonBe);
		} else if (isCabinetEmpty(cabinet, building)) {
			return addOrUpdate(
					educationLevel, course, group, subgroup, subject, lessonDate, lessonNum, willLessonBe
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

		TempSchedule tempSchedule = new TempSchedule();
		tempSchedule.setFlow(foundFlow.getId());
		tempSchedule.setSubject(foundSubject.getId());
		tempSchedule.setCabinet(foundCabinet.getId());
		tempSchedule.setLessonDate(lessonDate);
		tempSchedule.setLessonNum(lessonNum);
		tempSchedule.setWillLessonBe(willLessonBe);
		
		if (tempScheduleRepo
				.findByFlowAndLessonDateAndLessonNum(foundFlow.getId(), lessonDate, lessonNum)
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
		if (isSubjectEmpty(subject)) {
			return addOrUpdate(educationLevel, course, group, subgroup, lessonDate, lessonNum, willLessonBe);
		} else if (isTeacherEmpty(surname, name, patronymic) && isCabinetEmpty(cabinet, building)) {
			return addOrUpdate(
					educationLevel, course, group, subgroup, subject, lessonDate, lessonNum, willLessonBe
			);
		} else if (isTeacherEmpty(surname, name, patronymic)) {
			return addOrUpdate(
					educationLevel, course, group, subgroup, subject, cabinet, building,
					lessonDate, lessonNum, willLessonBe
			);
		} else if (isCabinetEmpty(cabinet, building)) {
			return addOrUpdate(
					educationLevel, course, group, subgroup, subject, surname, name, patronymic,
					lessonDate, lessonNum, willLessonBe
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

		TempSchedule tempSchedule = new TempSchedule();
		tempSchedule.setFlow(foundFlow.getId());
		tempSchedule.setSubject(foundSubject.getId());
		tempSchedule.setTeacher(foundTeacher.getId());
		tempSchedule.setCabinet(foundCabinet.getId());
		tempSchedule.setLessonDate(lessonDate);
		tempSchedule.setLessonNum(lessonNum);
		tempSchedule.setWillLessonBe(willLessonBe);
		
		if (tempScheduleRepo
				.findByFlowAndLessonDateAndLessonNum(foundFlow.getId(), lessonDate, lessonNum)
				.isPresent())
			return tempScheduleRepo.update(tempSchedule);
		
		return tempScheduleRepo.add(tempSchedule);
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

		return tempScheduleRepo
				.findByFlowAndLessonDateAndLessonNum(foundFlow.getId(), lessonDate, lessonNum)
				.orElseThrow();
	}

	@Override
	public TempSchedule deleteByFlowAndLessonDateAndLessonNum(
			int educationLevel, int course, int group, int subgroup, LocalDate lessonDate, int lessonNum
	) {
		Flow foundFlow = flowRepo
				.findByEducationLevelAndCourseAndGroupAndSubgroup(educationLevel, course, group, subgroup)
				.orElseThrow();
		
		TempSchedule foundSchedule = tempScheduleRepo
				.findByFlowAndLessonDateAndLessonNum(foundFlow.getId(), lessonDate, lessonNum)
				.orElseThrow();
		
		return deleteById(foundSchedule.getId());
	}
	
	private boolean isSubjectEmpty(String subject) {
		return subject == null || subject.isBlank();
	}
	
	private boolean isTeacherEmpty(String surname, String name, String patronymic) {
		return (surname == null || surname.isBlank()) && (name == null || name.isBlank())
				&& (patronymic == null || patronymic.isBlank());
	}
	
	private boolean isCabinetEmpty(String cabinet, String building) {
		return (cabinet == null || cabinet.isBlank()) && (building == null || building.isBlank());
	}

}
