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
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class HomeworkController {
	private final HomeworkService homeworkService;
	private final FlowService flowService;
	private final UserService userService;
	
	@GetMapping("/all")
	public ResponseEntity<List<HomeworkResponse>> getAllHomeworks() {
		log.info("Received request to get all homeworks");
		
		Collection<Homework> found = homeworkService.findAll();
		List<HomeworkResponse> result = new ArrayList<>();
		found.forEach((e) -> {
			if (e instanceof HomeworkJoined) {
				HomeworkJoined homework = (HomeworkJoined) e;
				
				FlowResponse flowResponse = new FlowResponse(
						homework.getJoinedFlow().getFlowLvl(), homework.getJoinedFlow().getCourse(),
						homework.getJoinedFlow().getFlow(), homework.getJoinedFlow().getSubgroup(),
						homework.getJoinedFlow().getLastEdit(), homework.getJoinedFlow().getLessonsStartDate(),
						homework.getJoinedFlow().getSessionStartDate(), homework.getJoinedFlow().getSessionEndDate(),
						homework.getJoinedFlow().isActive()
				);
				
				result.add(new HomeworkResponse(
						homework.getHomework(), homework.getLessonDate(), homework.getLessonNum(),
						flowResponse, homework.getLessonName()
				));
			} else {
				Flow flow = flowService.findById(e.getFlow());
				FlowResponse flowResponse = new FlowResponse(
						flow.getFlowLvl(), flow.getCourse(), flow.getFlow(), flow.getSubgroup(),
						flow.getLastEdit(), flow.getLessonsStartDate(), flow.getSessionStartDate(),
						flow.getSessionEndDate(), flow.isActive()
				);
						
				result.add(new HomeworkResponse(
						e.getHomework(), e.getLessonDate(), e.getLessonNum(), flowResponse, e.getLessonName()
				));
			}
		});
		
		log.info(String.format("Found all (%s) homeworks: %s", found.size(), found));
		
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("flow")
	public ResponseEntity<List<HomeworkResponse>> getAllHomeworksByFlow(@RequestBody FlowRequest flow) {
		log.info("Received request to get all homeworks by flow %s", flow);
		
		Collection<Homework> found = homeworkService.findAllByFlow(
				flow.flow_lvl(), flow.course(), flow.flow(), flow.subgroup()
		);
		List<HomeworkResponse> result = new ArrayList<>();
		found.forEach((e) -> {
			if (e instanceof HomeworkJoined) {
				HomeworkJoined homework = (HomeworkJoined) e;
				
				FlowResponse flowResponse = new FlowResponse(
						homework.getJoinedFlow().getFlowLvl(), homework.getJoinedFlow().getCourse(),
						homework.getJoinedFlow().getFlow(), homework.getJoinedFlow().getSubgroup(),
						homework.getJoinedFlow().getLastEdit(), homework.getJoinedFlow().getLessonsStartDate(),
						homework.getJoinedFlow().getSessionStartDate(), homework.getJoinedFlow().getSessionEndDate(),
						homework.getJoinedFlow().isActive()
				);
				
				result.add(new HomeworkResponse(
						homework.getHomework(), homework.getLessonDate(), homework.getLessonNum(),
						flowResponse, homework.getLessonName()
				));
			} else {
				Flow foundFlow = flowService.findById(e.getFlow());
				FlowResponse flowResponse = new FlowResponse(
						foundFlow.getFlowLvl(), foundFlow.getCourse(), foundFlow.getFlow(), foundFlow.getSubgroup(),
						foundFlow.getLastEdit(), foundFlow.getLessonsStartDate(), foundFlow.getSessionStartDate(),
						foundFlow.getSessionEndDate(), foundFlow.isActive()
				);
						
				result.add(new HomeworkResponse(
						e.getHomework(), e.getLessonDate(), e.getLessonNum(), flowResponse, e.getLessonName()
				));
			}
		});
		
		log.info(String.format("Found all (%s) homeworks by flow %s: %s", found.size(), flow, found));
		
		return ResponseEntity.ok(result);
	}
	
	@PostMapping("/homework")
	public ResponseEntity<?> addHomework(
			@RequestHeader("api_key") String apiKey, @RequestBody HomeworkRequest homework
	) {
		log.info(String.format("Received request from \"%s\" to add homework %s", apiKey, homework));
		
		if (!userService.checkFlowAccessByApiKey(
				apiKey, homework.flow().flow_lvl(), homework.flow().course(),
				homework.flow().flow(), homework.flow().subgroup()
		)) throw new NoAccessException("Нет доступа для добавления д/з");

		log.info(String.format("User \"%s\" is trying to add homework %s", apiKey, homework));
		
		Homework addedHomework = homeworkService.addOrUpdate(
				homework.homework(), homework.lesson_date(), homework.lesson_num(), homework.flow().flow_lvl(),
				homework.flow().course(), homework.flow().flow(), homework.flow().subgroup(), homework.lesson_name()
		);
		
		log.info(String.format("User \"%s\" has successfully added homework %s", apiKey, addedHomework));
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/homework")
	public ResponseEntity<?> deleteHomework(
			@RequestHeader("api_key") String apiKey, @RequestBody DeleteHomeworkRequest homework
	) {
		log.info(String.format("Received request from \"%s\" to delete homework %s", apiKey, homework));
		
		if (!userService.checkFlowAccessByApiKey(
				apiKey, homework.flow().flow_lvl(), homework.flow().course(),
				homework.flow().flow(), homework.flow().subgroup()
		)) throw new NoAccessException("Нет доступа для добавления д/з");

		log.info(String.format("User \"%s\" is trying to delete homework %s", apiKey, homework));
		
		Homework deletedHomework = homeworkService.delete(
				homework.flow().flow_lvl(), homework.flow().course(), homework.flow().flow(),
				homework.flow().subgroup(), homework.lesson_date(), homework.lesson_num()
		);
		
		log.info(String.format("User \"%s\" has successfully deleted homework %s", apiKey, deletedHomework));
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/homeworks")
	@Transactional
	public ResponseEntity<?> deleteHomeworks(
			@RequestHeader("api_key") String apiKey, @RequestBody List<DeleteHomeworkRequest> homeworks
	) {
		log.info(String.format(
				"Received request from \"%s\" to delete %s homeworks: %s", apiKey, homeworks.size(), homeworks
		));
		
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

		log.info(String.format(
				"User \"%s\" is trying to delete %s homeworks: %s", apiKey, homeworks.size(), homeworks
		));
		
		List<Homework> deletedHomeworks = new ArrayList<>();
		for (DeleteHomeworkRequest homework : homeworks) {
			Homework deletedHomework = homeworkService.delete(
					homework.flow().flow_lvl(), homework.flow().course(), homework.flow().flow(),
					homework.flow().subgroup(), homework.lesson_date(), homework.lesson_num()
			);
			deletedHomeworks.add(deletedHomework);
		}
		
		log.info(String.format(
				"User \"%s\" has successfully deleted %s homeworks: %s",
				apiKey, deletedHomeworks.size(), deletedHomeworks
		));
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/all")
	public ResponseEntity<?> deleteAllHomeworks(@RequestHeader("api_key") String apiKey) {
		log.info(String.format("Received request from \"%s\" to delete all homeworks", apiKey));
		
		if (!userService.checkAdminAccessByApiKey(apiKey))
			throw new NoAccessException("Нет доступа для удаления д/з");
		
		log.info(String.format("User \"%s\" is trying to delete all homeworks", apiKey));
		
		Collection<Homework> deletedHomeworks = homeworkService.deleteAll();

		log.info(String.format(
				"User \"%s\" has successfully deleted all (%s) homeworks: %s",
				apiKey, deletedHomeworks.size(), deletedHomeworks
		));
		
		return ResponseEntity.ok().build();
	}
}
