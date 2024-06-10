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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.schedule.dto.flow.FlowRequest;
import ru.khanin.dmitrii.schedule.dto.flow.FlowResponse;
import ru.khanin.dmitrii.schedule.dto.lesson.LessonResponse;
import ru.khanin.dmitrii.schedule.dto.temp.DeleteTempScheduleRequest;
import ru.khanin.dmitrii.schedule.dto.temp.TempScheduleRequest;
import ru.khanin.dmitrii.schedule.dto.temp.TempScheduleResponse;
import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.entity.Lesson;
import ru.khanin.dmitrii.schedule.entity.TempSchedule;
import ru.khanin.dmitrii.schedule.service.FlowService;
import ru.khanin.dmitrii.schedule.service.LessonService;
import ru.khanin.dmitrii.schedule.service.TempScheduleService;

@RestController
@RequestMapping("temp")
@RequiredArgsConstructor
public class TempScheduleController {
	private final TempScheduleService tempScheduleService;
	private final FlowService flowService;
	private final LessonService lessonService;
	
	@GetMapping("/all")
	public ResponseEntity<List<TempScheduleResponse>> getAllTempSchedules() {
		Collection<TempSchedule> found = tempScheduleService.findAll();
		List<TempScheduleResponse> result = new ArrayList<>();
		found.forEach((e) -> {
			Flow flow = flowService.findById(e.getFlow());
			FlowResponse flowResponse = new FlowResponse(
					flow.getFlowLvl(), flow.getCourse(), flow.getFlow(), flow.getSubgroup()
			);
					
			Lesson lesson = lessonService.findById(e.getLesson());
			LessonResponse lessonResponse = new LessonResponse(
					lesson.getName(), lesson.getTeacher(), lesson.getCabinet()
			);
			
			result.add(new TempScheduleResponse(
					flowResponse, lessonResponse, e.getLessonDate(), e.getLessonNum(), e.isWillLessonBe()
			));
		});
		
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/flow")
	public ResponseEntity<List<TempScheduleResponse>> getAllTempSchedulesByFlow(@RequestBody FlowRequest flow) {
		Collection<TempSchedule> found = tempScheduleService
				.findAllByFlow(flow.flow_lvl(), flow.course(), flow.flow(), flow.subgroup());
		List<TempScheduleResponse> result = new ArrayList<>();
		found.forEach((e) -> {
			Flow foundFlow = flowService.findById(e.getFlow());
			FlowResponse flowResponse = new FlowResponse(
					foundFlow.getFlowLvl(), foundFlow.getCourse(), foundFlow.getFlow(), foundFlow.getSubgroup()
			);

			Lesson lesson = lessonService.findById(e.getLesson());
			LessonResponse lessonResponse = new LessonResponse(
					lesson.getName(), lesson.getTeacher(), lesson.getCabinet()
			);
			
			result.add(new TempScheduleResponse(
					flowResponse, lessonResponse, e.getLessonDate(), e.getLessonNum(), e.isWillLessonBe()
			));
		});
		
		return ResponseEntity.ok(result);
	}
	
	@PostMapping("/schedule")
	public ResponseEntity<?> addTempSchedule(@RequestBody TempScheduleRequest tempSchedule) {
		tempScheduleService.add(
				tempSchedule.flow().flow_lvl(), tempSchedule.flow().course(), tempSchedule.flow().flow(),
				tempSchedule.flow().subgroup(), tempSchedule.lesson().name(), tempSchedule.lesson().teacher(),
				tempSchedule.lesson().cabinet(), tempSchedule.lesson_date(), tempSchedule.lesson_num(),
				tempSchedule.will_lesson_be()
		);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/schedules")
	@Transactional
	public ResponseEntity<?> addTempSchedules(@RequestBody List<TempScheduleRequest> tempSchedules) {
		for (TempScheduleRequest tempSchedule : tempSchedules) {
			tempScheduleService.add(
					tempSchedule.flow().flow_lvl(), tempSchedule.flow().course(), tempSchedule.flow().flow(),
					tempSchedule.flow().subgroup(), tempSchedule.lesson().name(), tempSchedule.lesson().teacher(),
					tempSchedule.lesson().cabinet(), tempSchedule.lesson_date(), tempSchedule.lesson_num(),
					tempSchedule.will_lesson_be()
			);
		}
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/schedule")
	public ResponseEntity<?> deleteTempSchedule(@RequestBody DeleteTempScheduleRequest tempSchedule) {
		tempScheduleService.delete(
				tempSchedule.flow().flow_lvl(), tempSchedule.flow().course(), tempSchedule.flow().flow(),
				tempSchedule.flow().subgroup(), tempSchedule.lesson_date(), tempSchedule.lesson_num()
		);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/schedules")
	@Transactional
	public ResponseEntity<?> deleteSchedules(@RequestBody List<DeleteTempScheduleRequest> tempSchedules) {
		for (DeleteTempScheduleRequest tempSchedule : tempSchedules) {
			tempScheduleService.delete(
					tempSchedule.flow().flow_lvl(), tempSchedule.flow().course(), tempSchedule.flow().flow(),
					tempSchedule.flow().subgroup(), tempSchedule.lesson_date(), tempSchedule.lesson_num()
					);
		}
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/all")
	public ResponseEntity<?> deleteAllTempSchedules() {
		tempScheduleService.deleteAll();
		return ResponseEntity.ok().build();
	}
}
