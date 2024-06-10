package ru.khanin.dmitrii.schedule.service.jdbc;

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
	public Schedule add(long flowId, long lessonId, int dayOfWeek, int lessonNum, boolean isNumerator) {
		Schedule schedule = new Schedule();
		schedule.setFlow(flowId);
		schedule.setLesson(lessonId);
		schedule.setDayOfWeek(dayOfWeek);
		schedule.setLessonNum(lessonNum);
		schedule.setNumerator(isNumerator);
		return scheduleRepo.add(schedule);
	}
	
	@Override
	public Schedule add(
			int flowLvl, int course, int flow, int subgroup, long lessonId,
			int dayOfWeek, int lessonNum, boolean isNumerator
	) {
		Schedule schedule = new Schedule();
		schedule.setLesson(lessonId);
		schedule.setDayOfWeek(dayOfWeek);
		schedule.setLessonNum(lessonNum);
		schedule.setNumerator(isNumerator);
		
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
			
			schedule.setFlow(addedFlow.getId());
		} else {
			schedule.setFlow(foundFlow.getId());
		}
		
		return scheduleRepo.add(schedule);
	}
	
	@Override
	public Schedule add(
			long flowId, String name, String teacher, String cabinet,
			int dayOfWeek, int lessonNum, boolean isNumerator
	) {
		Schedule schedule = new Schedule();
		schedule.setFlow(flowId);
		schedule.setDayOfWeek(dayOfWeek);
		schedule.setLessonNum(lessonNum);
		schedule.setNumerator(isNumerator);
		
		Lesson foundLesson = lessonRepo
				.findByNameAndTeacherAndCabinet(name, teacher, cabinet)
				.orElse(null);
		if (foundLesson == null) {
			Lesson lessonToAdd = new Lesson();
			lessonToAdd.setName(name);
			lessonToAdd.setTeacher(teacher);
			lessonToAdd.setCabinet(cabinet);
			Lesson addedLesson = lessonRepo.add(lessonToAdd);
			
			schedule.setLesson(addedLesson.getId());
		} else {
			schedule.setLesson(foundLesson.getId());
		}
		
		return scheduleRepo.add(schedule);
	}
	
	@Override
	public Schedule add(
			int flowLvl, int course, int flow, int subgroup,
			String name, String teacher, String cabinet,
			int dayOfWeek, int lessonNum, boolean isNumerator
	) {
		Schedule schedule = new Schedule();
		schedule.setDayOfWeek(dayOfWeek);
		schedule.setLessonNum(lessonNum);
		schedule.setNumerator(isNumerator);
		
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
			
			schedule.setFlow(addedFlow.getId());
		} else {
			schedule.setFlow(foundFlow.getId());
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
			
			schedule.setLesson(addedLesson.getId());
		} else {
			schedule.setLesson(foundLesson.getId());
		}
		
		return scheduleRepo.add(schedule);
	}

	@Override
	public Schedule findByFlowAndDayOfWeekAndLessonNumAndIsNumerator(
			long flowId, int dayOfWeek, int lessonNum, boolean isNumerator
	) {
		return scheduleRepo
				.findByFlowAndDayOfWeekAndLessonNumAndIsNumerator(
						flowId, dayOfWeek, lessonNum, isNumerator
				)
				.orElse(null);
	}

	@Override
	public Collection<Schedule> findAll() {
		Iterable<Schedule> found = scheduleRepo.findAll();
		Collection<Schedule> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}

	@Override
	public Collection<Schedule> findAllByFlow(long flowId) {
		Iterable<Schedule> found = scheduleRepo.findAllByFlow(flowId);
		Collection<Schedule> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}
	
	@Override
	public Collection<Schedule> findAllByFlow(int flowLvl, int course, int flow, int subgroup) {
		Flow foundFlow = flowRepo
				.findByFlowLvlAndCourseAndFlowAndSubgroup(flowLvl, course, flow, subgroup)
				.orElse(null);
		if (foundFlow == null) return null;
		
		return findAllByFlow(foundFlow.getId());
	}

	@Override
	public Collection<Schedule> findAllByFlowAndDayOfWeekAndIsNumerator(
			long flowId, int dayOfWeek, boolean isNumerator
	) {
		Iterable<Schedule> found = scheduleRepo.findAllByFlowAndDayOfWeekAndIsNumerator(
				flowId, dayOfWeek, isNumerator
		);
		Collection<Schedule> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}
	
	@Override
	public Collection<Schedule> findAllWhereTeacherStartsWith(String teacher) {
		Iterable<Schedule> found = scheduleRepo.findAllWhereTeacherStartsWith(teacher);
		Collection<Schedule> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}
	
	@Override
	public Schedule delete(int flowLvl, int course, int flow, int subgroup, int dayOfWeek, int lessonNum,
			boolean isNumerator) {
		Flow foundFlow = flowRepo
				.findByFlowLvlAndCourseAndFlowAndSubgroup(flowLvl, course, flow, subgroup)
				.orElse(null);
		if (foundFlow == null) return null;
		
		Schedule foundSchedule = scheduleRepo
				.findByFlowAndDayOfWeekAndLessonNumAndIsNumerator(foundFlow.getId(), dayOfWeek, lessonNum, isNumerator)
				.orElse(null);
		if (foundSchedule == null) return null;
		
		return deleteById(foundSchedule.getId());
	}

	@Override
	public Schedule deleteById(long id) {
		return scheduleRepo.deleteById(id).orElse(null);
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
