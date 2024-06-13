package ru.khanin.dmitrii.schedule.service;

import java.time.LocalDate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OutdatedScheduler {
	private final HomeworkService homeworkService;
	private final TempScheduleService tempScheduleService;
	
	@Scheduled(cron = "${app.cron}")
	public void deleteOutdated() {
		LocalDate now = LocalDate.now();
		homeworkService.deleteAllBeforeDate(now);
		tempScheduleService.deleteAllBeforeDate(now);
	}
}
