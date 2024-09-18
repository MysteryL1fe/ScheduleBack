package ru.khanin.dmitrii.schedule.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
			log.error("Exception in getting brs flows: " + e.getMessage());
			return;
		}
		
		if (studGroups == null || studGroups.isEmpty()) return;
		
		List<Flow> brsFlows = new ArrayList<>();
		for (StudGroupResponse studGroup : studGroups) {
			try {
				brsFlows.addAll(convertStudGroupToFlows(studGroup));
			} catch (Exception e) {
				log.error("Exception in convert brs flow: " + e.getMessage());
			}
		}
		
		Collection<Flow> foundFlows = flowService.findAllActive();
		
		for (Flow brsFlow: brsFlows) {
			boolean found = false;
			
			for (Flow foundFlow : foundFlows) {
				if (foundFlow.equalsByFlowData(brsFlow) && foundFlow.equalsByDates(brsFlow)) {
					found = true;
					break;
				}
			}
			
			if (!found) {
				flowService.addOrUpdate(
						brsFlow.getEducationLevel(), brsFlow.getCourse(), brsFlow.getGroup(), brsFlow.getSubgroup(),
						brsFlow.getLessonsStartDate(), brsFlow.getSessionStartDate(),
						brsFlow.getSessionEndDate(), true
				);
			}
		}
		
		log.info("Flows from brs saved, check old active flows");
		
		for (Flow flow : foundFlows) {
			boolean exists = false;
			
			for (Flow brsFlow : brsFlows) {
				if (flow.equalsByFlowData(brsFlow)) {
					exists = true;
					break;
				}
			}
			
			if (!exists) {
				flowService.addOrUpdate(
						flow.getEducationLevel(), flow.getCourse(), flow.getGroup(), flow.getSubgroup(), false
				);			
			}
		}
		
		log.info("Stud groups update has ended");
	}
	
	private List<Flow> convertStudGroupToFlows(StudGroupResponse studGroupResponse) {
		List<Flow> result = new ArrayList<>();
		if (studGroupResponse.sub_count() == 0) {
			Flow flow = new Flow();
			flow.setEducationLevel(convertEducationLevel(studGroupResponse.education_level()));
			flow.setCourse(studGroupResponse.course());
			flow.setGroup(studGroupResponse.num());
			flow.setSubgroup(1);
			flow.setLessonsStartDate(studGroupResponse.lessons_start_date());
			flow.setSessionStartDate(studGroupResponse.session_start_date());
			flow.setSessionEndDate(studGroupResponse.session_end_date());
			result.add(flow);
			
			return result;
		}
		
		for (int i = 1; i <= studGroupResponse.sub_count(); i++) {
			Flow flow = new Flow();
			flow.setEducationLevel(1);
			flow.setCourse(studGroupResponse.course());
			flow.setGroup(studGroupResponse.num());
			flow.setSubgroup(i);
			flow.setLessonsStartDate(studGroupResponse.lessons_start_date());
			flow.setSessionStartDate(studGroupResponse.session_start_date());
			flow.setSessionEndDate(studGroupResponse.session_end_date());
			result.add(flow);
		}
		
		return result;
	}
	
	private int convertEducationLevel(String educationLevel) {
		return switch (educationLevel) {
		case "bachelor", "specialist":
			yield 1;
		default:
			throw new IllegalArgumentException("Unexpected value: " + educationLevel);
		};
	}
}
