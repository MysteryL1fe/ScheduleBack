package ru.khanin.dmitrii.schedule.service;

import java.time.LocalDate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutdatedScheduler {
	private final HomeworkService homeworkService;
	private final TempScheduleService tempScheduleService;
	
	@Scheduled(cron = "${app.cron}")
	public void deleteOutdated() {
		log.info("Delete outdated data has started");
		
		LocalDate now = LocalDate.now();
		homeworkService.deleteAllBeforeDate(now);
		tempScheduleService.deleteAllBeforeDate(now);
		
		log.info("Delete outdated data has ended");
	}
}
