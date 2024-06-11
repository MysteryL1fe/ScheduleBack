package ru.khanin.dmitrii.schedule.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.schedule.client.BrsClient;
import ru.khanin.dmitrii.schedule.dto.brs.StudGroupResponse;
import ru.khanin.dmitrii.schedule.entity.Flow;

@Service
@RequiredArgsConstructor
public class BrsScheduler {
	private final FlowService flowService;
	private final BrsClient brsClient;
	
	@Scheduled(cron = "${app.cron}")
	public void checkStudGroups() {
		List<StudGroupResponse> studGroups = brsClient.getStudGroups();
		List<Flow> brsFlows = new ArrayList<>();
		for (StudGroupResponse studGroup : studGroups) {
			brsFlows.addAll(convertStudGroupsToFlows(studGroup));
		}
		
		Collection<Flow> flows = flowService.findAll();
		
		if (flows.size() != brsFlows.size())  {
			updateFlows(brsFlows);
			return;
		}
		
		for (Flow flow : flows) {
			for (Flow brsFlow : brsFlows) {
				if (!flow.equalsWithoutId(brsFlow)) {
					updateFlows(brsFlows);
					return;
				}
			}
		}
		
		for (Flow brsFlow : brsFlows) {
			for (Flow flow : flows) {
				if (!flow.equalsWithoutId(brsFlow)) {
					updateFlows(brsFlows);
					return;
				}
			}
		}
	}
	
	@Transactional
	private void updateFlows(List<Flow> newFlows) {
		flowService.deleteAll();
		for (Flow flow : newFlows) {
			flowService.add(flow.getFlowLvl(), flow.getCourse(), flow.getFlow(), flow.getSubgroup());
		}
	}
	
	private List<Flow> convertStudGroupsToFlows(StudGroupResponse studGroupResponse) {
		List<Flow> result = new ArrayList<>();
		if (studGroupResponse.sub_count() == 0) {
			Flow flow = new Flow();
			flow.setFlowLvl(1);
			flow.setCourse(studGroupResponse.course());
			flow.setFlow(studGroupResponse.num());
			flow.setSubgroup(1);
			result.add(flow);
			
			return result;
		}
		
		for (int i = 1; i <= studGroupResponse.sub_count(); i++) {
			Flow flow = new Flow();
			flow.setFlowLvl(1);
			flow.setCourse(studGroupResponse.course());
			flow.setFlow(studGroupResponse.num());
			flow.setSubgroup(i);
			result.add(flow);
		}
		
		return result;
	}
}
