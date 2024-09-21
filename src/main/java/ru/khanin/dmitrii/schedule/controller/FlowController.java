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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.khanin.dmitrii.schedule.dto.flow.AddFlowRequest;
import ru.khanin.dmitrii.schedule.dto.flow.FlowResponse;
import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.service.BrsScheduler;
import ru.khanin.dmitrii.schedule.service.FlowService;

@RestController()
@RequestMapping("/flow")
@RequiredArgsConstructor
@Slf4j
public class FlowController {
	private final FlowService flowService;
	private final BrsScheduler brsScheduler;

	@GetMapping("/all")
	public ResponseEntity<List<FlowResponse>> getAllFlows() {
		log.trace("Received request to get all flows");
		
		Collection<Flow> found = flowService.findAll();
		List<FlowResponse> result = new ArrayList<>();
		found.forEach(e -> result.add(convertFlowToResponse(e)));
		
		log.trace(String.format("Found all (%s) flows: %s", found.size(), found));
		
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/active")
	public ResponseEntity<List<FlowResponse>> getAllActiveFlows() {
		log.trace("Received request to get all active flows");
		
		Collection<Flow> found = flowService.findAllActive();
		List<FlowResponse> result = new ArrayList<>();
		found.forEach(e -> result.add(convertFlowToResponse(e)));

		log.trace(String.format("Found all (%s) active flows: %s", found.size(), found));
		
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/flow")
	public ResponseEntity<FlowResponse> getFlow(
			@RequestParam(name = "education_level") int educationLevel, @RequestParam int course,
			@RequestParam int group, @RequestParam int subgroup
	) {
		log.trace(String.format("Received request to get flow %s.%s.%s.%s", educationLevel, course, group, subgroup));
		
		Flow found = flowService.findByEducationLevelAndCourseAndGroupAndSubgroup(
				educationLevel, course, group, subgroup
		);
		FlowResponse response = convertFlowToResponse(found);

		log.trace(String.format("Found flow: %s", found));
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/brs")
	public ResponseEntity<?> updateFlowsFromBrs() {
		log.info(String.format("Received request to update stud groups"));
		
		brsScheduler.checkStudGroups();

		log.info(String.format("User \"%s\" has successfully updated stud groups"));
		
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/flow")
	public ResponseEntity<?> addSingleFlow(@RequestBody AddFlowRequest flow) {
		log.trace(String.format("Received request to add flow %s", flow));
		
		Flow addedFlow = flowService.addOrUpdate(
				flow.education_level(), flow.course(), flow.group(), flow.subgroup(),
				flow.lessons_start_date(), flow.session_start_date(), flow.session_end_date(),
				flow.active()
		);
		
		log.trace(String.format("Successfully added flow %s", addedFlow));
		
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/flows")
	@Transactional
	public ResponseEntity<?> addFlows(@RequestBody List<AddFlowRequest> flows) {
		log.trace(String.format("Received request to add %s flows %s", flows.size(), flows));
		
		List<Flow> addedFlows = new ArrayList<>();
		for (AddFlowRequest flow : flows) {
			Flow addedFlow = flowService.addOrUpdate(
					flow.education_level(), flow.course(), flow.group(), flow.subgroup(),
					flow.lessons_start_date(), flow.session_start_date(), flow.session_end_date(),
					flow.active()
			);
			addedFlows.add(addedFlow);
		}
		
		log.trace(String.format("Successfully added %s flows: %s", addedFlows.size(), addedFlows));
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/all")
	public ResponseEntity<?> deleteAllFlows() {
		log.trace(String.format("Received request to delete all flows"));
		
		Collection<Flow> deletedFlows = flowService.deleteAll();

		log.trace(String.format(
				"Successfully deleted all (%s) flows: %s", deletedFlows.size(), deletedFlows
		));
		
		return ResponseEntity.ok().build();
	}
	
	private FlowResponse convertFlowToResponse(Flow flow) {
		return new FlowResponse(
				flow.getEducationLevel(), flow.getCourse(), flow.getGroup(), flow.getSubgroup(), flow.getLastEdit(),
				flow.getLessonsStartDate(), flow.getSessionStartDate(), flow.getSessionEndDate(), flow.isActive()
		);
	}
}
