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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.khanin.dmitrii.schedule.dto.flow.FlowResponse;
import ru.khanin.dmitrii.schedule.dto.lesson.LessonResponse;
import ru.khanin.dmitrii.schedule.dto.temp.DeleteTempScheduleRequest;
import ru.khanin.dmitrii.schedule.dto.temp.TempScheduleRequest;
import ru.khanin.dmitrii.schedule.dto.temp.TempScheduleResponse;
import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.entity.Lesson;
import ru.khanin.dmitrii.schedule.entity.TempSchedule;
import ru.khanin.dmitrii.schedule.entity.jdbc.TempScheduleJoined;
import ru.khanin.dmitrii.schedule.exception.NoAccessException;
import ru.khanin.dmitrii.schedule.service.FlowService;
import ru.khanin.dmitrii.schedule.service.LessonService;
import ru.khanin.dmitrii.schedule.service.TempScheduleService;
import ru.khanin.dmitrii.schedule.service.UserService;

@RestController
@RequestMapping("temp")
@RequiredArgsConstructor
@Slf4j
public class TempScheduleController {
	private final TempScheduleService tempScheduleService;
	private final FlowService flowService;
	private final LessonService lessonService;
	private final UserService userService;
	
	@GetMapping("/all")
	public ResponseEntity<List<TempScheduleResponse>> getAllTempSchedules() {
		log.info("Received request to get all temp schedules");
		
		Collection<TempSchedule> found = tempScheduleService.findAll();
		List<TempScheduleResponse> result = new ArrayList<>();
		found.forEach((e) -> {
			if (e instanceof TempScheduleJoined) {
				TempScheduleJoined schedule = (TempScheduleJoined) e;
				
				FlowResponse flowResponse = new FlowResponse(
						schedule.getFlowJoined().getFlowLvl(), schedule.getFlowJoined().getCourse(),
						schedule.getFlowJoined().getFlow(), schedule.getFlowJoined().getSubgroup(),
						schedule.getFlowJoined().getLastEdit(), schedule.getFlowJoined().getLessonsStartDate(),
						schedule.getFlowJoined().getSessionStartDate(), schedule.getFlowJoined().getSessionEndDate(),
						schedule.getFlowJoined().isActive()
				);
				
				LessonResponse lessonResponse = new LessonResponse(
						schedule.getLessonJoined().getName(), schedule.getLessonJoined().getTeacher(),
						schedule.getLessonJoined().getCabinet()
				);
				
				result.add(new TempScheduleResponse(
						flowResponse, lessonResponse, schedule.getLessonDate(),
						schedule.getLessonNum(), schedule.isWillLessonBe()
				));
			} else {
				Flow flow = flowService.findById(e.getFlow());
				FlowResponse flowResponse = new FlowResponse(
						flow.getFlowLvl(), flow.getCourse(), flow.getFlow(), flow.getSubgroup(),
						flow.getLastEdit(), flow.getLessonsStartDate(), flow.getSessionStartDate(),
						flow.getSessionEndDate(), flow.isActive()
				);
						
				Lesson lesson = lessonService.findById(e.getLesson());
				LessonResponse lessonResponse = new LessonResponse(
						lesson.getName(), lesson.getTeacher(), lesson.getCabinet()
				);
				
				result.add(new TempScheduleResponse(
						flowResponse, lessonResponse, e.getLessonDate(), e.getLessonNum(), e.isWillLessonBe()
				));
			}
		});
		
		log.info(String.format("Found all (%s) temp schedules: %s", found.size(), found));
		
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/flow")
	public ResponseEntity<List<TempScheduleResponse>> getAllTempSchedulesByFlow(
			@RequestParam(name = "flow_lvl") int flowLvl, @RequestParam int course,
			@RequestParam int flow, @RequestParam int subgroup
	) {
		log.info(String.format(
				"Received request to get all temp schedules by flow %s.%s.%s.%s", flowLvl, course, flow, subgroup
		));
		
		Collection<TempSchedule> found = tempScheduleService.findAllByFlow(flowLvl, course, flow, subgroup);
		List<TempScheduleResponse> result = new ArrayList<>();
		found.forEach((e) -> {
			if (e instanceof TempScheduleJoined) {
				TempScheduleJoined schedule = (TempScheduleJoined) e;
				
				FlowResponse flowResponse = new FlowResponse(
						schedule.getFlowJoined().getFlowLvl(), schedule.getFlowJoined().getCourse(),
						schedule.getFlowJoined().getFlow(), schedule.getFlowJoined().getSubgroup(),
						schedule.getFlowJoined().getLastEdit(), schedule.getFlowJoined().getLessonsStartDate(),
						schedule.getFlowJoined().getSessionStartDate(), schedule.getFlowJoined().getSessionEndDate(),
						schedule.getFlowJoined().isActive()
				);
				
				LessonResponse lessonResponse = new LessonResponse(
						schedule.getLessonJoined().getName(), schedule.getLessonJoined().getTeacher(),
						schedule.getLessonJoined().getCabinet()
				);
				
				result.add(new TempScheduleResponse(
						flowResponse, lessonResponse, schedule.getLessonDate(),
						schedule.getLessonNum(), schedule.isWillLessonBe()
				));
			} else {
				Flow foundFlow = flowService.findById(e.getFlow());
				FlowResponse flowResponse = new FlowResponse(
						foundFlow.getFlowLvl(), foundFlow.getCourse(), foundFlow.getFlow(), foundFlow.getSubgroup(),
						foundFlow.getLastEdit(), foundFlow.getLessonsStartDate(), foundFlow.getSessionStartDate(),
						foundFlow.getSessionEndDate(), foundFlow.isActive()
				);
	
				Lesson lesson = lessonService.findById(e.getLesson());
				LessonResponse lessonResponse = new LessonResponse(
						lesson.getName(), lesson.getTeacher(), lesson.getCabinet()
				);
				
				result.add(new TempScheduleResponse(
						flowResponse, lessonResponse, e.getLessonDate(), e.getLessonNum(), e.isWillLessonBe()
				));
			}
		});
		
		log.info(String.format("Found all (%s) temp schedules by flow %s: %s", found.size(), flow, found));
		
		return ResponseEntity.ok(result);
	}
	
	@PostMapping("/schedule")
	public ResponseEntity<?> addTempSchedule(
			@RequestHeader("api_key") String apiKey, @RequestBody TempScheduleRequest tempSchedule
	) {
		log.info(String.format("Received request from \"%s\" to add temp schedule %s", apiKey, tempSchedule));
		
		if (!userService.checkFlowAccessByApiKey(
				apiKey, tempSchedule.flow().flow_lvl(), tempSchedule.flow().course(),
				tempSchedule.flow().flow(), tempSchedule.flow().subgroup()
		)) throw new NoAccessException("Нет доступа для добавления временного занятия");

		log.info(String.format("User \"%s\" is trying to add temp schedule %s", apiKey, tempSchedule));
		
		TempSchedule addedSchedule = tempScheduleService.addOrUpdate(
				tempSchedule.flow().flow_lvl(), tempSchedule.flow().course(), tempSchedule.flow().flow(),
				tempSchedule.flow().subgroup(), tempSchedule.lesson().name(), tempSchedule.lesson().teacher(),
				tempSchedule.lesson().cabinet(), tempSchedule.lesson_date(), tempSchedule.lesson_num(),
				tempSchedule.will_lesson_be()
		);
		
		log.info(String.format("User \"%s\" has successfully added temp schedule %s", apiKey, addedSchedule));
		
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/schedules")
	@Transactional
	public ResponseEntity<?> addTempSchedules(
			@RequestHeader("api_key") String apiKey, @RequestBody List<TempScheduleRequest> tempSchedules
	) {
		log.info(String.format(
				"Received request from \"%s\" to add %s temp schedules %s", apiKey, tempSchedules.size(), tempSchedules
		));
		
		List<Flow> flows = new ArrayList<>();
		tempSchedules.forEach((e) -> {
			Flow flow = new Flow();
			flow.setFlowLvl(e.flow().flow_lvl());
			flow.setCourse(e.flow().course());
			flow.setFlow(e.flow().flow());
			flow.setSubgroup(e.flow().subgroup());
			
			flows.add(flow);
		});
		if (!userService.checkFlowsAccessByApiKey(apiKey, flows))
			throw new NoAccessException("Нет доступа для добавления временных занятий");

		log.info(String.format(
				"User \"%s\" is trying to add %s temp schedules: %s", apiKey, tempSchedules.size(), tempSchedules
		));
		
		List<TempSchedule> addedSchedules = new ArrayList<>();
		for (TempScheduleRequest tempSchedule : tempSchedules) {
			TempSchedule addedSchedule = tempScheduleService.addOrUpdate(
					tempSchedule.flow().flow_lvl(), tempSchedule.flow().course(), tempSchedule.flow().flow(),
					tempSchedule.flow().subgroup(), tempSchedule.lesson().name(), tempSchedule.lesson().teacher(),
					tempSchedule.lesson().cabinet(), tempSchedule.lesson_date(), tempSchedule.lesson_num(),
					tempSchedule.will_lesson_be()
			);
			addedSchedules.add(addedSchedule);
		}
		
		log.info(String.format(
				"User \"%s\" has successfully added %s temp schedules: %s",
				apiKey, addedSchedules.size(), addedSchedules
		));
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/schedule")
	public ResponseEntity<?> deleteTempSchedule(
			@RequestHeader("api_key") String apiKey, @RequestBody DeleteTempScheduleRequest tempSchedule
	) {
		log.info(String.format("Received request from \"%s\" to delete temp schedule %s", apiKey, tempSchedule));
		
		if (!userService.checkFlowAccessByApiKey(
				apiKey, tempSchedule.flow().flow_lvl(), tempSchedule.flow().course(),
				tempSchedule.flow().flow(), tempSchedule.flow().subgroup()
		)) throw new NoAccessException("Нет доступа для удаления временного занятия");

		log.info(String.format("User \"%s\" is trying to delete temp schedule %s", apiKey, tempSchedule));
		
		TempSchedule deletedSchedule = tempScheduleService.delete(
				tempSchedule.flow().flow_lvl(), tempSchedule.flow().course(), tempSchedule.flow().flow(),
				tempSchedule.flow().subgroup(), tempSchedule.lesson_date(), tempSchedule.lesson_num()
		);
		
		log.info(String.format("User \"%s\" has successfully deleted temp schedule %s", apiKey, deletedSchedule));
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/schedules")
	@Transactional
	public ResponseEntity<?> deleteTempSchedules(
			@RequestHeader("api_key") String apiKey, @RequestBody List<DeleteTempScheduleRequest> tempSchedules
	) {
		log.info(String.format(
				"Received request from \"%s\" to delete %s temp schedules: %s",
				apiKey, tempSchedules.size(), tempSchedules
		));
		
		List<Flow> flows = new ArrayList<>();
		tempSchedules.forEach((e) -> {
			Flow flow = new Flow();
			flow.setFlowLvl(e.flow().flow_lvl());
			flow.setCourse(e.flow().course());
			flow.setFlow(e.flow().flow());
			flow.setSubgroup(e.flow().subgroup());
			
			flows.add(flow);
		});
		if (!userService.checkFlowsAccessByApiKey(apiKey, flows))
			throw new NoAccessException("Нет доступа для удаления временных занятий");

		log.info(String.format(
				"User \"%s\" is trying to delete %s temp schedules: %s", apiKey, tempSchedules.size(), tempSchedules
		));
		
		List<TempSchedule> deletedSchedules = new ArrayList<>();
		for (DeleteTempScheduleRequest tempSchedule : tempSchedules) {
			TempSchedule deletedSchedule = tempScheduleService.delete(
					tempSchedule.flow().flow_lvl(), tempSchedule.flow().course(), tempSchedule.flow().flow(),
					tempSchedule.flow().subgroup(), tempSchedule.lesson_date(), tempSchedule.lesson_num()
			);
			deletedSchedules.add(deletedSchedule);
		}
		
		log.info(String.format(
				"User \"%s\" has successfully deleted %s temp schedules: %s",
				apiKey, deletedSchedules.size(), deletedSchedules
		));
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/all")
	public ResponseEntity<?> deleteAllTempSchedules(@RequestHeader("api_key") String apiKey) {
		log.info(String.format("Received request from \"%s\" to delete all temp schedules", apiKey));
		
		if (!userService.checkAdminAccessByApiKey(apiKey))
			throw new NoAccessException("Нет доступа для удаления временных занятий");
		
		log.info(String.format("User \"%s\" is trying to delete all temp schedules", apiKey));
		
		Collection<TempSchedule> deletedSchedules = tempScheduleService.deleteAll();

		log.info(String.format(
				"User \"%s\" has successfully deleted all (%s) temp schedules: %s",
				apiKey, deletedSchedules.size(), deletedSchedules
		));
		
		return ResponseEntity.ok().build();
	}
}
