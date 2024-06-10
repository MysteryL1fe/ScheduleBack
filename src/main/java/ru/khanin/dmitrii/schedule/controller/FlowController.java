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
import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.service.FlowService;

@RestController()
@RequestMapping("flow")
@RequiredArgsConstructor
public class FlowController {
	private final FlowService flowService;

	@GetMapping("/all")
	public ResponseEntity<List<FlowResponse>> getAllFlows() {
		Collection<Flow> found = flowService.findAll();
		List<FlowResponse> result = new ArrayList<>();
		found.forEach((e) -> result.add(new FlowResponse(e.getFlowLvl(), e.getCourse(), e.getFlow(), e.getSubgroup())));
		return ResponseEntity.ok(result);
	}
	
	@PostMapping("/flow")
	public ResponseEntity<?> addSingleFlow(@RequestBody FlowRequest flow) {
		flowService.add(flow.flow_lvl(), flow.course(), flow.flow(), flow.subgroup());
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/flows")
	@Transactional
	public ResponseEntity<?> addFlows(@RequestBody List<FlowRequest> flows) {
		for (FlowRequest flow : flows) {
			flowService.add(flow.flow_lvl(), flow.course(), flow.flow(), flow.subgroup());
		}
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/all")
	public ResponseEntity<?> deleteAllFlows() {
		flowService.deleteAll();
		return ResponseEntity.ok().build();
	}
}
