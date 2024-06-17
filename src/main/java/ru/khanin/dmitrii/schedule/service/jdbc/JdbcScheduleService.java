package ru.khanin.dmitrii.schedule.service.jdbc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.entity.Lesson;
import ru.khanin.dmitrii.schedule.entity.Schedule;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcFlowRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcLessonRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcScheduleRepo;
import ru.khanin.dmitrii.schedule.service.ScheduleService;

@RequiredArgsConstructor
public class JdbcScheduleService implements ScheduleService {
	private final JdbcScheduleRepo scheduleRepo;
	private final JdbcFlowRepo flowRepo;
	private final JdbcLessonRepo lessonRepo;

	@Override
	public Schedule addOrUpdate(long flowId, long lessonId, int dayOfWeek, int lessonNum, boolean numerator) {
		Schedule schedule = new Schedule();
		schedule.setFlow(flowId);
		schedule.setLesson(lessonId);
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
			int flowLvl, int course, int flow, int subgroup, long lessonId,
			int dayOfWeek, int lessonNum, boolean numerator
	) {
		Schedule schedule = new Schedule();
		schedule.setLesson(lessonId);
		schedule.setDayOfWeek(dayOfWeek);
		schedule.setLessonNum(lessonNum);
		schedule.setNumerator(numerator);
		
		Flow foundFlow = flowRepo
				.findByFlowLvlAndCourseAndFlowAndSubgroup(flowLvl, course, flow, subgroup)
				.orElseGet(() -> {
					Flow flowToAdd = new Flow();
					flowToAdd.setFlowLvl(flowLvl);
					flowToAdd.setCourse(course);
					flowToAdd.setFlow(flow);
					flowToAdd.setSubgroup(subgroup);
					flowToAdd.setLastEdit(LocalDateTime.now());
					Flow addedFlow = flowRepo.add(flowToAdd);
					
					return addedFlow;
				});
		
		return addOrUpdate(foundFlow.getId(), lessonId, dayOfWeek, lessonNum, numerator);
	}
	
	@Override
	@Transactional
	public Schedule addOrUpdate(
			long flowId, String name, String teacher, String cabinet,
			int dayOfWeek, int lessonNum, boolean numerator
	) {
		Schedule schedule = new Schedule();
		schedule.setFlow(flowId);
		schedule.setDayOfWeek(dayOfWeek);
		schedule.setLessonNum(lessonNum);
		schedule.setNumerator(numerator);
		
		Lesson foundLesson = lessonRepo
				.findByNameAndTeacherAndCabinet(name, teacher, cabinet)
				.orElseGet(() -> {
					Lesson lessonToAdd = new Lesson();
					lessonToAdd.setName(name);
					lessonToAdd.setTeacher(teacher);
					lessonToAdd.setCabinet(cabinet);
					Lesson addedLesson = lessonRepo.add(lessonToAdd);
					
					return addedLesson;
				});
		
		return addOrUpdate(flowId, foundLesson.getId(), dayOfWeek, lessonNum, numerator);
	}
	
	@Override
	@Transactional
	public Schedule addOrUpdate(
			int flowLvl, int course, int flow, int subgroup,
			String name, String teacher, String cabinet,
			int dayOfWeek, int lessonNum, boolean numerator
	) {
		Schedule schedule = new Schedule();
		schedule.setDayOfWeek(dayOfWeek);
		schedule.setLessonNum(lessonNum);
		schedule.setNumerator(numerator);
		
		Flow foundFlow = flowRepo
				.findByFlowLvlAndCourseAndFlowAndSubgroup(flowLvl, course, flow, subgroup)
				.orElseGet(() -> {
					Flow flowToAdd = new Flow();
					flowToAdd.setFlowLvl(flowLvl);
					flowToAdd.setCourse(course);
					flowToAdd.setFlow(flow);
					flowToAdd.setSubgroup(subgroup);
					flowToAdd.setLastEdit(LocalDateTime.now());
					Flow addedFlow = flowRepo.add(flowToAdd);
					
					return addedFlow;
				});
		
		Lesson foundLesson = lessonRepo
				.findByNameAndTeacherAndCabinet(name, teacher, cabinet)
				.orElseGet(() -> {
					Lesson lessonToAdd = new Lesson();
					lessonToAdd.setName(name);
					lessonToAdd.setTeacher(teacher);
					lessonToAdd.setCabinet(cabinet);
					Lesson addedLesson = lessonRepo.add(lessonToAdd);
					
					return addedLesson;
				});
		
		return addOrUpdate(foundFlow.getId(), foundLesson.getId(), dayOfWeek, lessonNum, numerator);
	}

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
	public Collection<Schedule> findAllByFlow(int flowLvl, int course, int flow, int subgroup) {
		Flow foundFlow = flowRepo
				.findByFlowLvlAndCourseAndFlowAndSubgroup(flowLvl, course, flow, subgroup)
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
	public Collection<Schedule> findAllWhereTeacherStartsWith(String teacher) {
		Iterable<? extends Schedule> found = scheduleRepo.findAllWhereTeacherStartsWith(teacher);
		Collection<Schedule> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}
	
	@Override
	public Schedule delete(int flowLvl, int course, int flow, int subgroup, int dayOfWeek, int lessonNum,
			boolean numerator) {
		Flow foundFlow = flowRepo
				.findByFlowLvlAndCourseAndFlowAndSubgroup(flowLvl, course, flow, subgroup)
				.orElseThrow();
		
		Schedule foundSchedule = scheduleRepo
				.findByFlowAndDayOfWeekAndLessonNumAndNumerator(foundFlow.getId(), dayOfWeek, lessonNum, numerator)
				.orElseThrow();
		
		return deleteById(foundSchedule.getId());
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

}
