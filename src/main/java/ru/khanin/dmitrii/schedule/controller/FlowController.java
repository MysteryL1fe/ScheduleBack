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
import ru.khanin.dmitrii.schedule.dto.flow.AddFlowRequest;
import ru.khanin.dmitrii.schedule.dto.flow.FlowResponse;
import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.exception.NoAccessException;
import ru.khanin.dmitrii.schedule.service.FlowService;
import ru.khanin.dmitrii.schedule.service.UserService;

@RestController()
@RequestMapping("flow")
@RequiredArgsConstructor
@Slf4j
public class FlowController {
	private final FlowService flowService;
	private final UserService userService;

	@GetMapping("/all")
	public ResponseEntity<List<FlowResponse>> getAllFlows() {
		log.info("Received request to get all flows");
		
		Collection<Flow> found = flowService.findAll();
		List<FlowResponse> result = new ArrayList<>();
		found.forEach((e) -> {
			result.add(new FlowResponse(
					e.getFlowLvl(), e.getCourse(), e.getFlow(), e.getSubgroup(), e.getLastEdit(),
					e.getLessonsStartDate(), e.getSessionStartDate(), e.getSessionEndDate(),
					e.isActive()
			));
		});
		
		log.info(String.format("Found all (%s) flows: %s", found.size(), found));
		
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/active")
	public ResponseEntity<List<FlowResponse>> getAllActiveFlows() {
		log.info("Received request to get all active flows");
		
		Collection<Flow> found = flowService.findAllActive();
		List<FlowResponse> result = new ArrayList<>();
		found.forEach((e) -> {
			result.add(new FlowResponse(
					e.getFlowLvl(), e.getCourse(), e.getFlow(), e.getSubgroup(), e.getLastEdit(),
					e.getLessonsStartDate(), e.getSessionStartDate(), e.getSessionEndDate(),
					e.isActive()
			));
		});

		log.info(String.format("Found all (%s) active flows: %s", found.size(), found));
		
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/flow")
	public ResponseEntity<FlowResponse> getFlow(
			@RequestParam(name = "flow_lvl") int flowLvl, @RequestParam int course,
			@RequestParam int flow, @RequestParam int subgroup
	) {
		log.info(String.format("Received request to get flow %s.%s.%s.%s", flowLvl, course, flow, subgroup));
		
		Flow found = flowService.findByFlowLvlAndCourseAndFlowAndSubgroup(flowLvl, course, flow, subgroup);
		FlowResponse response = new FlowResponse(
				found.getFlowLvl(), found.getCourse(), found.getFlow(), found.getSubgroup(), found.getLastEdit(),
				found.getLessonsStartDate(), found.getSessionStartDate(), found.getSessionEndDate(),
				found.isActive()
		);

		log.info(String.format("Found flow: %s", found));
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/flow")
	public ResponseEntity<?> addSingleFlow(@RequestHeader("api_key") String apiKey, @RequestBody AddFlowRequest flow) {
		log.info(String.format("Received request from \"%s\" to add flow %s", apiKey, flow));
		
		if (!userService.checkAdminAccessByApiKey(apiKey))
			throw new NoAccessException("Нет доступа для добавления группы");
		
		log.info(String.format("User \"%s\" is trying to add flow %s", apiKey, flow));
		
		Flow addedFlow = flowService.addOrUpdate(
				flow.flow_lvl(), flow.course(), flow.flow(), flow.subgroup(),
				flow.lessons_start_date(), flow.session_start_date(), flow.session_end_date(),
				flow.active()
		);
		
		log.info(String.format("User \"%s\" has successfully added flow %s", apiKey, addedFlow));
		
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/flows")
	@Transactional
	public ResponseEntity<?> addFlows(
			@RequestHeader("api_key") String apiKey, @RequestBody List<AddFlowRequest> flows
	) {
		log.info(String.format("Received request from \"%s\" to add %s flows %s", apiKey, flows.size(), flows));
		
		if (!userService.checkAdminAccessByApiKey(apiKey))
			throw new NoAccessException("Нет доступа для добавления групп");

		log.info(String.format("User \"%s\" is trying to add %s flows: %s", apiKey, flows.size(), flows));
		
		List<Flow> addedFlows = new ArrayList<>();
		for (AddFlowRequest flow : flows) {
			Flow addedFlow = flowService.addOrUpdate(
					flow.flow_lvl(), flow.course(), flow.flow(), flow.subgroup(),
					flow.lessons_start_date(), flow.session_start_date(), flow.session_end_date(),
					flow.active()
			);
			addedFlows.add(addedFlow);
		}
		
		log.info(String.format(
				"User \"%s\" has successfully added %s flows: %s", apiKey, addedFlows.size(), addedFlows
		));
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/all")
	public ResponseEntity<?> deleteAllFlows(@RequestHeader("api_key") String apiKey) {
		log.info(String.format("Received request from \"%s\" to delete all flows", apiKey));
		
		if (!userService.checkAdminAccessByApiKey(apiKey))
			throw new NoAccessException("Нет доступа для удаления групп");
		
		log.info(String.format("User \"%s\" is trying to delete all flows", apiKey));
		
		Collection<Flow> deletedFlows = flowService.deleteAll();

		log.info(String.format(
				"User \"%s\" has successfully deleted all (%s) flows: %s", apiKey, deletedFlows.size(), deletedFlows
		));
		
		return ResponseEntity.ok().build();
	}
}
