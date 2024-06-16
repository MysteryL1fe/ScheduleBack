package ru.khanin.dmitrii.schedule.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.khanin.dmitrii.schedule.client.BrsClient;
import ru.khanin.dmitrii.schedule.dto.brs.StudGroupResponse;
import ru.khanin.dmitrii.schedule.entity.Flow;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrsScheduler {
	private final FlowService flowService;
	private final BrsClient brsClient;
	
	@Scheduled(cron = "${app.cron}")
	@Transactional
	public void checkStudGroups() {
		log.info("Stud groups update has started");
		
		List<StudGroupResponse> studGroups = null;
		try {
			studGroups = brsClient.getStudGroups();
		} catch (Exception e) {
			log.info("Exception catched");
			return;
		}
		
		if (studGroups == null || studGroups.isEmpty()) return;
		
		List<Flow> brsFlows = new ArrayList<>();
		for (StudGroupResponse studGroup : studGroups)
			brsFlows.addAll(convertStudGroupToFlows(studGroup));
		
		for (Flow brsFlow : brsFlows) {
			try {
				Flow flow = flowService.findByFlowLvlAndCourseAndFlowAndSubgroup(
						brsFlow.getFlowLvl(), brsFlow.getCourse(), brsFlow.getFlow(), brsFlow.getSubgroup()
				);
				if (!(flow.equalsByFlowData(brsFlow) && flow.equalsByDates(brsFlow) && flow.isActive())) {
					flowService.addOrUpdate(
							brsFlow.getFlowLvl(), brsFlow.getCourse(), brsFlow.getFlow(), brsFlow.getSubgroup(),
							brsFlow.getLessonsStartDate(), brsFlow.getSessionStartDate(),
							brsFlow.getSessionEndDate(), true
					);	
				}
			} catch (NoSuchElementException e) {
				flowService.addOrUpdate(
						brsFlow.getFlowLvl(), brsFlow.getCourse(), brsFlow.getFlow(), brsFlow.getSubgroup(),
						brsFlow.getLessonsStartDate(), brsFlow.getSessionStartDate(),
						brsFlow.getSessionEndDate(), true
				);
			}
		}
		
		log.info("Active flows are set, check active flows");
		
		Collection<Flow> activeFlows = flowService.findAllActive();
		for (Flow flow : activeFlows) {
			boolean exists = false;
			for (Flow brsFlow : brsFlows) {
				if (flow.equalsByFlowData(brsFlow)) {
					exists = true;
					break;
				}
			}
			if (!exists) {
				flowService.addOrUpdate(
						flow.getFlowLvl(), flow.getCourse(), flow.getFlow(), flow.getSubgroup(), false
				);			
			}
		}
		
		log.info("Stud groups update has ended");
	}
	
	private List<Flow> convertStudGroupToFlows(StudGroupResponse studGroupResponse) {
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
