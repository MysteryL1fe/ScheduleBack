package ru.khanin.dmitrii.schedule.service.jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.entity.Lesson;
import ru.khanin.dmitrii.schedule.entity.TempSchedule;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcFlowRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcLessonRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcTempScheduleRepo;
import ru.khanin.dmitrii.schedule.service.TempScheduleService;

@RequiredArgsConstructor
public class JdbcTempScheduleService implements TempScheduleService {
	private final JdbcTempScheduleRepo tempScheduleRepo;
	private final JdbcFlowRepo flowRepo;
	private final JdbcLessonRepo lessonRepo;

	@Override
	public TempSchedule add(long flowId, long lessonId, LocalDate lessonDate, int lessonNum, boolean willLessonBe) {
		TempSchedule tempSchedule = new TempSchedule();
		tempSchedule.setFlow(flowId);
		tempSchedule.setLesson(lessonId);
		tempSchedule.setLessonDate(lessonDate);
		tempSchedule.setLessonNum(lessonNum);
		tempSchedule.setWillLessonBe(willLessonBe);
		return tempScheduleRepo.add(tempSchedule);
	}
	
	@Override
	public TempSchedule add(int flowLvl, int course, int flow, int subgroup, long lessonId, LocalDate lessonDate,
			int lessonNum, boolean willLessonBe) {
		TempSchedule tempSchedule = new TempSchedule();
		tempSchedule.setLesson(lessonId);
		tempSchedule.setLessonDate(lessonDate);
		tempSchedule.setLessonNum(lessonNum);
		tempSchedule.setWillLessonBe(willLessonBe);
		
		Flow foundFlow = flowRepo
				.findByFlowLvlAndCourseAndFlowAndSubgroup(flowLvl, course, flow, subgroup)
				.orElse(null);
		if (foundFlow == null) {
			Flow flowToAdd = new Flow();
			flowToAdd.setFlowLvl(flowLvl);
			flowToAdd.setCourse(course);
			flowToAdd.setFlow(flow);
			flowToAdd.setSubgroup(subgroup);
			Flow addedFlow = flowRepo.add(flowToAdd);
			
			tempSchedule.setFlow(addedFlow.getId());
		} else {
			tempSchedule.setFlow(foundFlow.getId());
		}
		
		return tempScheduleRepo.add(tempSchedule);
	}
	
	@Override
	public TempSchedule add(long flowId, String name, String teacher, String cabinet, LocalDate lessonDate,
			int lessonNum, boolean willLessonBe) {
		TempSchedule tempSchedule = new TempSchedule();
		tempSchedule.setFlow(flowId);
		tempSchedule.setLessonDate(lessonDate);
		tempSchedule.setLessonNum(lessonNum);
		tempSchedule.setWillLessonBe(willLessonBe);

		Lesson foundLesson = lessonRepo
				.findByNameAndTeacherAndCabinet(name, teacher, cabinet)
				.orElse(null);
		if (foundLesson == null) {
			Lesson lessonToAdd = new Lesson();
			lessonToAdd.setName(name);
			lessonToAdd.setTeacher(teacher);
			lessonToAdd.setCabinet(cabinet);
			Lesson addedLesson = lessonRepo.add(lessonToAdd);
			
			tempSchedule.setLesson(addedLesson.getId());
		} else {
			tempSchedule.setLesson(foundLesson.getId());
		}
		
		return tempScheduleRepo.add(tempSchedule);
	}
	
	@Override
	public TempSchedule add(int flowLvl, int course, int flow, int subgroup, String name, String teacher,
			String cabinet, LocalDate lessonDate, int lessonNum, boolean willLessonBe) {
		TempSchedule tempSchedule = new TempSchedule();
		tempSchedule.setLessonDate(lessonDate);
		tempSchedule.setLessonNum(lessonNum);
		tempSchedule.setWillLessonBe(willLessonBe);
		
		Flow foundFlow = flowRepo
				.findByFlowLvlAndCourseAndFlowAndSubgroup(flowLvl, course, flow, subgroup)
				.orElse(null);
		if (foundFlow == null) {
			Flow flowToAdd = new Flow();
			flowToAdd.setFlowLvl(flowLvl);
			flowToAdd.setCourse(course);
			flowToAdd.setFlow(flow);
			flowToAdd.setSubgroup(subgroup);
			Flow addedFlow = flowRepo.add(flowToAdd);
			
			tempSchedule.setFlow(addedFlow.getId());
		} else {
			tempSchedule.setFlow(foundFlow.getId());
		}
		
		Lesson foundLesson = lessonRepo
				.findByNameAndTeacherAndCabinet(name, teacher, cabinet)
				.orElse(null);
		if (foundLesson == null) {
			Lesson lessonToAdd = new Lesson();
			lessonToAdd.setName(name);
			lessonToAdd.setTeacher(teacher);
			lessonToAdd.setCabinet(cabinet);
			Lesson addedLesson = lessonRepo.add(lessonToAdd);
			
			tempSchedule.setLesson(addedLesson.getId());
		} else {
			tempSchedule.setLesson(foundLesson.getId());
		}
		
		return tempScheduleRepo.add(tempSchedule);
	}

	@Override
	public TempSchedule findByFlowAndLessonDateAndLessonNum(long flowId, LocalDate lessonDate, int lessonNum) {
		return tempScheduleRepo
				.findByFlowAndLessonDateAndLessonNum(flowId, lessonDate, lessonNum)
				.orElse(null);
	}

	@Override
	public Collection<TempSchedule> findAll() {
		Iterable<TempSchedule> found = tempScheduleRepo.findAll();
		Collection<TempSchedule> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}
	
	@Override
	public Collection<TempSchedule> findAllByFlow(long flowId) {
		Iterable<TempSchedule> found = tempScheduleRepo.findAllByFlow(flowId);
		Collection<TempSchedule> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}
	
	@Override
	public Collection<TempSchedule> findAllByFlow(int flowLvl, int course, int flow, int subgroup) {
		Flow foundFlow = flowRepo
				.findByFlowLvlAndCourseAndFlowAndSubgroup(flowLvl, course, flow, subgroup)
				.orElse(null);
		if (foundFlow == null) return null;
		
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
	public Collection<TempSchedule> findAllByFlowAndLessonDate(int flowLvl, int course, int flow, int subgroup,
			LocalDate lessonDate) {
		Flow foundFlow = flowRepo
				.findByFlowLvlAndCourseAndFlowAndSubgroup(flowLvl, course, flow, subgroup)
				.orElse(null);
		if (foundFlow == null) return null;
		
		return findAllByFlowAndLessonDate(foundFlow.getId(), lessonDate);
	}
	
	@Override
	public TempSchedule delete(int flowLvl, int course, int flow, int subgroup, LocalDate lessonDate, int lessonNum) {
		Flow foundFlow = flowRepo
				.findByFlowLvlAndCourseAndFlowAndSubgroup(flowLvl, course, flow, subgroup)
				.orElse(null);
		if (foundFlow == null) return null;
		
		TempSchedule foundSchedule = tempScheduleRepo
				.findByFlowAndLessonDateAndLessonNum(foundFlow.getId(), lessonDate, lessonNum)
				.orElse(null);
		if (foundSchedule == null) return null;
		
		return deleteById(foundSchedule.getId());
	}

	@Override
	public TempSchedule deleteById(long id) {
		return tempScheduleRepo.deleteById(id).orElse(null);
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

}
