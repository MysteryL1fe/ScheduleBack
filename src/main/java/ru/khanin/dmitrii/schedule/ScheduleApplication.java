package ru.khanin.dmitrii.schedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import ru.khanin.dmitrii.schedule.configuration.AppConfig;
import ru.khanin.dmitrii.schedule.service.BrsScheduler;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(AppConfig.class)
public class ScheduleApplication {
	public static void main(String[] args) {
		var ctx = SpringApplication.run(ScheduleApplication.class);
		ctx.getBean(BrsScheduler.class).checkStudGroups();
	}
}
