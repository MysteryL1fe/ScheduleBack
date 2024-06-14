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
		List<StudGroupResponse> studGroups = null;
		try {
			studGroups = brsClient.getStudGroups();
		} catch (Exception e) {
			return;
		}
		
		if (studGroups == null || studGroups.isEmpty()) return;
		
		List<Flow> brsFlows = new ArrayList<>();
		for (StudGroupResponse studGroup : studGroups)
			brsFlows.addAll(convertStudGroupsToFlows(studGroup));
		Collection<Flow> flows = flowService.findAll();
		
		if (flows.size() != brsFlows.size())  {
			clearAndUpdateFlows(brsFlows);
			return;
		}
		
		for (Flow flow : flows) {
			boolean exists = false;
			for (Flow brsFlow : brsFlows) {
				if (flow.equalsByFlowData(brsFlow)) {
					exists = true;
					break;
				}
			}
			if (!exists) {
				clearAndUpdateFlows(brsFlows);
				return;				
			}
		}
		
		for (Flow brsFlow : brsFlows) {
			boolean exists = false;
			for (Flow flow : flows) {
				if (flow.equalsByFlowData(brsFlow)) {
					exists = true;
					break;
				}
			}
			if (!exists) {
				clearAndUpdateFlows(brsFlows);
				return;				
			}
		}
		
		for (Flow flow : flows) {
			boolean equals = false;
			for (Flow brsFlow : brsFlows) {
				if (flow.equalsByFlowData(brsFlow) && flow.equalsByDates(brsFlow)) {
					equals = true;
					break;
				}
			}
			if (!equals) {
				updateFlows(brsFlows);
				return;				
			}
		}
	}
	
	@Transactional
	private void updateFlows(List<Flow> flows) {
		for (Flow flow : flows) {
			flowService.addOrUpdate(
					flow.getFlowLvl(), flow.getCourse(), flow.getFlow(), flow.getSubgroup(),
					flow.getLessonsStartDate(), flow.getSessionStartDate(), flow.getSessionEndDate()
			);
		}
	}
	
	@Transactional
	private void clearAndUpdateFlows(List<Flow> newFlows) {
		flowService.deleteAll();
		updateFlows(newFlows);
	}
	
	private List<Flow> convertStudGroupsToFlows(StudGroupResponse studGroupResponse) {
		List<Flow> result = new ArrayList<>();
		if (studGroupResponse.sub_count() == 0) {
			Flow flow = new Flow();
			flow.setFlowLvl(1);
			flow.setCourse(studGroupResponse.course());
			flow.setFlow(studGroupResponse.num());
			flow.setSubgroup(1);
			flow.setLessonsStartDate(studGroupResponse.lessons_start_date());
			flow.setSessionStartDate(studGroupResponse.session_start_date());
			flow.setSessionEndDate(studGroupResponse.session_end_date());
			result.add(flow);
			
			return result;
		}
		
		for (int i = 1; i <= studGroupResponse.sub_count(); i++) {
			Flow flow = new Flow();
			flow.setFlowLvl(1);
			flow.setCourse(studGroupResponse.course());
			flow.setFlow(studGroupResponse.num());
			flow.setSubgroup(i);
			flow.setLessonsStartDate(studGroupResponse.lessons_start_date());
			flow.setSessionStartDate(studGroupResponse.session_start_date());
			flow.setSessionEndDate(studGroupResponse.session_end_date());
			result.add(flow);
		}
		
		return result;
	}
}
