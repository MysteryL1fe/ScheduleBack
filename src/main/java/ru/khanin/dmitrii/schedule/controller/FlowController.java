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
import ru.khanin.dmitrii.schedule.dto.flow.AddFlowRequest;
import ru.khanin.dmitrii.schedule.dto.flow.FlowRequest;
import ru.khanin.dmitrii.schedule.dto.flow.FlowResponse;
import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.exception.NoAccessException;
import ru.khanin.dmitrii.schedule.service.FlowService;
import ru.khanin.dmitrii.schedule.service.UserService;

@RestController()
@RequestMapping("flow")
@RequiredArgsConstructor
public class FlowController {
	private final FlowService flowService;
	private final UserService userService;

	@GetMapping("/all")
	public ResponseEntity<List<FlowResponse>> getAllFlows() {
		Collection<Flow> found = flowService.findAll();
		List<FlowResponse> result = new ArrayList<>();
		found.forEach((e) -> {
			result.add(new FlowResponse(
					e.getFlowLvl(), e.getCourse(), e.getFlow(), e.getSubgroup(), e.getLastEdit(),
					e.getLessonsStartDate(), e.getSessionStartDate(), e.getSessionEndDate(),
					e.isActive()
			));
		});
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/active")
	public ResponseEntity<List<FlowResponse>> getAllActiveFlows() {
		Collection<Flow> found = flowService.findAllActive();
		List<FlowResponse> result = new ArrayList<>();
		found.forEach((e) -> {
			result.add(new FlowResponse(
					e.getFlowLvl(), e.getCourse(), e.getFlow(), e.getSubgroup(), e.getLastEdit(),
					e.getLessonsStartDate(), e.getSessionStartDate(), e.getSessionEndDate(),
					e.isActive()
			));
		});
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/flow")
	public ResponseEntity<FlowResponse> getFlow(@RequestBody FlowRequest flow) {
		Flow found = flowService.findByFlowLvlAndCourseAndFlowAndSubgroup(
				flow.flow_lvl(), flow.course(), flow.flow(), flow.subgroup()
		);
		FlowResponse response = new FlowResponse(
				found.getFlowLvl(), found.getCourse(), found.getFlow(), found.getSubgroup(), found.getLastEdit(),
				found.getLessonsStartDate(), found.getSessionStartDate(), found.getSessionEndDate(),
				found.isActive()
		);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/flow")
	public ResponseEntity<?> addSingleFlow(@RequestHeader("api_key") String apiKey, @RequestBody AddFlowRequest flow) {
		if (!userService.checkAdminAccessByApiKey(apiKey))
			throw new NoAccessException("Нет доступа для добавления группы");
		
		flowService.addOrUpdate(
				flow.flow_lvl(), flow.course(), flow.flow(), flow.subgroup(),
				flow.lessons_start_date(), flow.session_start_date(), flow.session_end_date(),
				flow.active()
		);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/flows")
	@Transactional
	public ResponseEntity<?> addFlows(@RequestHeader("api_key") String apiKey, @RequestBody List<AddFlowRequest> flows) {
		if (!userService.checkAdminAccessByApiKey(apiKey))
			throw new NoAccessException("Нет доступа для добавления групп");
		
		for (AddFlowRequest flow : flows) {
			flowService.addOrUpdate(
					flow.flow_lvl(), flow.course(), flow.flow(), flow.subgroup(),
					flow.lessons_start_date(), flow.session_start_date(), flow.session_end_date(),
					flow.active()
			);
		}
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/all")
	public ResponseEntity<?> deleteAllFlows(@RequestHeader("api_key") String apiKey) {
		if (!userService.checkAdminAccessByApiKey(apiKey))
			throw new NoAccessException("Нет доступа для удаления групп");
		
		flowService.deleteAll();
		return ResponseEntity.ok().build();
	}
}
