package ru.khanin.dmitrii.schedule.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.schedule.dto.flow.FlowRequest;
import ru.khanin.dmitrii.schedule.dto.flow.FlowResponse;
import ru.khanin.dmitrii.schedule.dto.lesson.LessonResponse;
import ru.khanin.dmitrii.schedule.dto.schedule.DeleteScheduleRequest;
import ru.khanin.dmitrii.schedule.dto.schedule.ScheduleRequest;
import ru.khanin.dmitrii.schedule.dto.schedule.ScheduleResponse;
import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.entity.Lesson;
import ru.khanin.dmitrii.schedule.entity.Schedule;
import ru.khanin.dmitrii.schedule.entity.jdbc.ScheduleJoined;
import ru.khanin.dmitrii.schedule.exception.NoAccessException;
import ru.khanin.dmitrii.schedule.service.FlowService;
import ru.khanin.dmitrii.schedule.service.LessonService;
import ru.khanin.dmitrii.schedule.service.ScheduleService;
import ru.khanin.dmitrii.schedule.service.UserService;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {
	private final ScheduleService scheduleService;
	private final FlowService flowService;
	private final LessonService lessonService;
	private final UserService userService;
	
	@GetMapping("/all")
	public ResponseEntity<List<ScheduleResponse>> getAllSchedules() {
		Collection<Schedule> found = scheduleService.findAll();
		List<ScheduleResponse> result = new ArrayList<>();
		found.forEach((e) -> {
			if (e instanceof ScheduleJoined) {
				ScheduleJoined schedule = (ScheduleJoined) e;
				
				FlowResponse flowResponse = new FlowResponse(
						schedule.getFlowJoined().getFlowLvl(), schedule.getFlowJoined().getCourse(),
						schedule.getFlowJoined().getFlow(), schedule.getFlowJoined().getSubgroup()
				);
				
				LessonResponse lessonResponse = new LessonResponse(
						schedule.getLessonJoined().getName(), schedule.getLessonJoined().getTeacher(),
						schedule.getLessonJoined().getCabinet()
				);
				
				result.add(new ScheduleResponse(
						flowResponse, lessonResponse, schedule.getDayOfWeek(),
						schedule.getLessonNum(), schedule.isNumerator()
				));
			} else {
				Flow flow = flowService.findById(e.getFlow());
				FlowResponse flowResponse = new FlowResponse(
						flow.getFlowLvl(), flow.getCourse(), flow.getFlow(), flow.getSubgroup()
				);
						
				Lesson lesson = lessonService.findById(e.getLesson());
				LessonResponse lessonResponse = new LessonResponse(
						lesson.getName(), lesson.getTeacher(), lesson.getCabinet()
				);
						
				result.add(new ScheduleResponse(
						flowResponse, lessonResponse, e.getDayOfWeek(), e.getLessonNum(), e.isNumerator()
				));
			}
		});
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/flow")
	public ResponseEntity<List<ScheduleResponse>> getAllSchedulesByFlow(@RequestBody FlowRequest flow) {
		Collection<Schedule> found = scheduleService
				.findAllByFlow(flow.flow_lvl(), flow.course(), flow.flow(), flow.subgroup());
		List<ScheduleResponse> result = new ArrayList<>();
		found.forEach((e) -> {
			if (e instanceof ScheduleJoined) {
				ScheduleJoined schedule = (ScheduleJoined) e;
				
				FlowResponse flowResponse = new FlowResponse(
						schedule.getFlowJoined().getFlowLvl(), schedule.getFlowJoined().getCourse(),
						schedule.getFlowJoined().getFlow(), schedule.getFlowJoined().getSubgroup()
				);
				
				LessonResponse lessonResponse = new LessonResponse(
						schedule.getLessonJoined().getName(), schedule.getLessonJoined().getTeacher(),
						schedule.getLessonJoined().getCabinet()
				);
				
				result.add(new ScheduleResponse(
						flowResponse, lessonResponse, schedule.getDayOfWeek(),
						schedule.getLessonNum(), schedule.isNumerator()
				));
			} else {
				Flow foundflow = flowService.findById(e.getFlow());
				FlowResponse flowResponse = new FlowResponse(
						foundflow.getFlowLvl(), foundflow.getCourse(), foundflow.getFlow(), foundflow.getSubgroup()
				);
						
				Lesson lesson = lessonService.findById(e.getLesson());
				LessonResponse lessonResponse = new LessonResponse(
						lesson.getName(), lesson.getTeacher(), lesson.getCabinet()
				);
						
				result.add(new ScheduleResponse(
						flowResponse, lessonResponse, e.getDayOfWeek(), e.getLessonNum(), e.isNumerator()
				));
			}
		});
		
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/teacher")
	public ResponseEntity<List<ScheduleResponse>> getAllSchedulesWhereTeacherStartsWith(@RequestBody String teacher) {
		Collection<Schedule> found = scheduleService.findAllWhereTeacherStartsWith(teacher);
		List<ScheduleResponse> result = new ArrayList<>();
		found.forEach((e) -> {
			if (e instanceof ScheduleJoined) {
				ScheduleJoined schedule = (ScheduleJoined) e;
				
				FlowResponse flowResponse = new FlowResponse(
						schedule.getFlowJoined().getFlowLvl(), schedule.getFlowJoined().getCourse(),
						schedule.getFlowJoined().getFlow(), schedule.getFlowJoined().getSubgroup()
				);
				
				LessonResponse lessonResponse = new LessonResponse(
						schedule.getLessonJoined().getName(), schedule.getLessonJoined().getTeacher(),
						schedule.getLessonJoined().getCabinet()
				);
				
				result.add(new ScheduleResponse(
						flowResponse, lessonResponse, schedule.getDayOfWeek(),
						schedule.getLessonNum(), schedule.isNumerator()
				));
			} else {
				Flow foundflow = flowService.findById(e.getFlow());
				FlowResponse flowResponse = new FlowResponse(
						foundflow.getFlowLvl(), foundflow.getCourse(), foundflow.getFlow(), foundflow.getSubgroup()
				);
						
				Lesson lesson = lessonService.findById(e.getLesson());
				LessonResponse lessonResponse = new LessonResponse(
						lesson.getName(), lesson.getTeacher(), lesson.getCabinet()
				);
						
				result.add(new ScheduleResponse(
						flowResponse, lessonResponse, e.getDayOfWeek(), e.getLessonNum(), e.isNumerator()
				));
			}
		});
		
		return ResponseEntity.ok(result);
	}
	
	@PostMapping("/schedule")
	public ResponseEntity<?> addSchedule(
			@RequestHeader("api_key") String apiKey, @RequestBody ScheduleRequest schedule
	) {
		if (!userService.checkFlowAccessByApiKey(
				apiKey, schedule.flow().flow_lvl(), schedule.flow().course(),
				schedule.flow().flow(), schedule.flow().subgroup()
		)) throw new NoAccessException("Нет доступа для добавления занятия");
		
		scheduleService.addOrUpdate(
				schedule.flow().flow_lvl(), schedule.flow().course(), schedule.flow().flow(),
				schedule.flow().subgroup(), schedule.lesson().name(), schedule.lesson().teacher(),
				schedule.lesson().cabinet(), schedule.day_of_week(),
				schedule.lesson_num(), schedule.is_numerator()
		);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/schedules")
	@Transactional
	public ResponseEntity<?> addSchedules(
			@RequestHeader("api_key") String apiKey, @RequestBody List<ScheduleRequest> schedules
	) {
		List<Flow> flows = new ArrayList<>();
		schedules.forEach((e) -> {
			Flow flow = new Flow();
			flow.setFlowLvl(e.flow().flow_lvl());
			flow.setCourse(e.flow().course());
			flow.setFlow(e.flow().flow());
			flow.setSubgroup(e.flow().subgroup());
			
			flows.add(flow);
		});
		if (!userService.checkFlowsAccessByApiKey(apiKey, flows))
			throw new NoAccessException("Нет доступа для добавления занятий");
		
		for (ScheduleRequest schedule : schedules) {
			scheduleService.addOrUpdate(
					schedule.flow().flow_lvl(), schedule.flow().course(), schedule.flow().flow(),
					schedule.flow().subgroup(), schedule.lesson().name(), schedule.lesson().teacher(),
					schedule.lesson().cabinet(), schedule.day_of_week(),
					schedule.lesson_num(), schedule.is_numerator()
			);
		}
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/schedule")
	public ResponseEntity<?> deleteSchedule(
			@RequestHeader("api_key") String apiKey, @RequestBody DeleteScheduleRequest schedule
	) {
		if (!userService.checkFlowAccessByApiKey(
				apiKey, schedule.flow().flow_lvl(), schedule.flow().course(),
				schedule.flow().flow(), schedule.flow().subgroup()
		)) throw new NoAccessException("Нет доступа для удаления занятия");
		
		scheduleService.delete(
				schedule.flow().flow_lvl(), schedule.flow().course(), schedule.flow().flow(),
				schedule.flow().subgroup(), schedule.day_of_week(), schedule.lesson_num(), schedule.is_numerator()
		);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/schedules")
	@Transactional
	public ResponseEntity<?> deleteSchedules(
			@RequestHeader("api_key") String apiKey, @RequestBody List<DeleteScheduleRequest> schedules
	) {
		List<Flow> flows = new ArrayList<>();
		schedules.forEach((e) -> {
			Flow flow = new Flow();
			flow.setFlowLvl(e.flow().flow_lvl());
			flow.setCourse(e.flow().course());
			flow.setFlow(e.flow().flow());
			flow.setSubgroup(e.flow().subgroup());
			
			flows.add(flow);
		});
		if (!userService.checkFlowsAccessByApiKey(apiKey, flows))
			throw new NoAccessException("Нет доступа для удаления занятий");
		
		for (DeleteScheduleRequest schedule : schedules) {
			scheduleService.delete(
					schedule.flow().flow_lvl(), schedule.flow().course(), schedule.flow().flow(),
					schedule.flow().subgroup(), schedule.day_of_week(), schedule.lesson_num(), schedule.is_numerator()
					);
		}
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/all")
	public ResponseEntity<?> deleteAllSchedules(@RequestHeader("api_key") String apiKey) {
		if (!userService.checkAdminAccessByApiKey(apiKey))
			throw new NoAccessException("Нет доступа для удаления занятий");
		
		scheduleService.deleteAll();
		return ResponseEntity.ok().build();
	}
}
