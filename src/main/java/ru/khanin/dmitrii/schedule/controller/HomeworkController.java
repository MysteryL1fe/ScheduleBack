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
import ru.khanin.dmitrii.schedule.dto.homework.DeleteHomeworkRequest;
import ru.khanin.dmitrii.schedule.dto.homework.HomeworkRequest;
import ru.khanin.dmitrii.schedule.dto.homework.HomeworkResponse;
import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.entity.Homework;
import ru.khanin.dmitrii.schedule.entity.jdbc.HomeworkJoined;
import ru.khanin.dmitrii.schedule.exception.NoAccessException;
import ru.khanin.dmitrii.schedule.service.FlowService;
import ru.khanin.dmitrii.schedule.service.HomeworkService;
import ru.khanin.dmitrii.schedule.service.UserService;

@RestController
@RequestMapping("homework")
@RequiredArgsConstructor
public class HomeworkController {
	private final HomeworkService homeworkService;
	private final FlowService flowService;
	private final UserService userService;
	
	@GetMapping("/all")
	public ResponseEntity<List<HomeworkResponse>> getAllHomeworks() {
		Collection<Homework> found = homeworkService.findAll();
		List<HomeworkResponse> result = new ArrayList<>();
		found.forEach((e) -> {
			if (e instanceof HomeworkJoined) {
				HomeworkJoined homework = (HomeworkJoined) e;
				
				FlowResponse flowResponse = new FlowResponse(
						homework.getJoinedFlow().getFlowLvl(), homework.getJoinedFlow().getCourse(),
						homework.getJoinedFlow().getFlow(), homework.getJoinedFlow().getSubgroup()
				);
				
				result.add(new HomeworkResponse(
						homework.getHomework(), homework.getLessonDate(), homework.getLessonNum(),
						flowResponse, homework.getLessonName()
				));
			} else {
				Flow flow = flowService.findById(e.getFlow());
				FlowResponse flowResponse = new FlowResponse(
						flow.getFlowLvl(), flow.getCourse(), flow.getFlow(), flow.getSubgroup()
				);
						
				result.add(new HomeworkResponse(
						e.getHomework(), e.getLessonDate(), e.getLessonNum(), flowResponse, e.getLessonName()
				));
			}
		});
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("flow")
	public ResponseEntity<List<HomeworkResponse>> getAllHomeworksByFlow(@RequestBody FlowRequest flow) {
		Collection<Homework> found = homeworkService.findAllByFlow(
				flow.flow_lvl(), flow.course(), flow.flow(), flow.subgroup()
		);
		List<HomeworkResponse> result = new ArrayList<>();
		found.forEach((e) -> {
			if (e instanceof HomeworkJoined) {
				HomeworkJoined homework = (HomeworkJoined) e;
				
				FlowResponse flowResponse = new FlowResponse(
						homework.getJoinedFlow().getFlowLvl(), homework.getJoinedFlow().getCourse(),
						homework.getJoinedFlow().getFlow(), homework.getJoinedFlow().getSubgroup()
				);
				
				result.add(new HomeworkResponse(
						homework.getHomework(), homework.getLessonDate(), homework.getLessonNum(),
						flowResponse, homework.getLessonName()
				));
			} else {
				Flow foundFlow = flowService.findById(e.getFlow());
				FlowResponse flowResponse = new FlowResponse(
						foundFlow.getFlowLvl(), foundFlow.getCourse(), foundFlow.getFlow(), foundFlow.getSubgroup()
				);
						
				result.add(new HomeworkResponse(
						e.getHomework(), e.getLessonDate(), e.getLessonNum(), flowResponse, e.getLessonName()
				));
			}
		});
		return ResponseEntity.ok(result);
	}
	
	@PostMapping("/homework")
	public ResponseEntity<?> addHomework(
			@RequestHeader("api_key") String apiKey, @RequestBody HomeworkRequest homework
	) {
		if (!userService.checkFlowAccessByApiKey(
				apiKey, homework.flow().flow_lvl(), homework.flow().course(),
				homework.flow().flow(), homework.flow().subgroup()
		)) throw new NoAccessException("Нет доступа для добавления д/з");
		
		homeworkService.addOrUpdate(
				homework.homework(), homework.lesson_date(), homework.lesson_num(), homework.flow().flow_lvl(),
				homework.flow().course(), homework.flow().flow(), homework.flow().subgroup(), homework.lesson_name()
		);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/homework")
	public ResponseEntity<?> deleteHomework(
			@RequestHeader("api_key") String apiKey, @RequestBody DeleteHomeworkRequest homework
	) {
		if (!userService.checkFlowAccessByApiKey(
				apiKey, homework.flow().flow_lvl(), homework.flow().course(),
				homework.flow().flow(), homework.flow().subgroup()
		)) throw new NoAccessException("Нет доступа для добавления д/з");
		
		homeworkService.delete(
				homework.flow().flow_lvl(), homework.flow().course(), homework.flow().flow(),
				homework.flow().subgroup(), homework.lesson_date(), homework.lesson_num()
		);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/homeworks")
	@Transactional
	public ResponseEntity<?> deleteHomeworks(
			@RequestHeader("api_key") String apiKey, @RequestBody List<DeleteHomeworkRequest> homeworks
	) {
		List<Flow> flows = new ArrayList<>();
		homeworks.forEach((e) -> {
			Flow flow = new Flow();
			flow.setFlowLvl(e.flow().flow_lvl());
			flow.setCourse(e.flow().course());
			flow.setFlow(e.flow().flow());
			flow.setSubgroup(e.flow().subgroup());
			
			flows.add(flow);
		});
		if (!userService.checkFlowsAccessByApiKey(apiKey, flows))
			throw new NoAccessException("Нет доступа для удаления д/з");
		
		for (DeleteHomeworkRequest homework : homeworks) {
			homeworkService.delete(
					homework.flow().flow_lvl(), homework.flow().course(), homework.flow().flow(),
					homework.flow().subgroup(), homework.lesson_date(), homework.lesson_num()
			);
		}
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/all")
	public ResponseEntity<?> deleteAllHomeworks(@RequestHeader("api_key") String apiKey) {
		if (!userService.checkAdminAccessByApiKey(apiKey))
			throw new NoAccessException("Нет доступа для удаления д/з");
		
		homeworkService.deleteAll();
		return ResponseEntity.ok().build();
	}
}
