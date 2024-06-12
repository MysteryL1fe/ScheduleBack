package ru.khanin.dmitrii.schedule.configuration;

import java.net.URI;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.khanin.dmitrii.schedule.client.BrsClient;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcFlowRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcHomeworkRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcLessonRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcScheduleRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcTempScheduleRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcUserRepo;
import ru.khanin.dmitrii.schedule.service.FlowService;
import ru.khanin.dmitrii.schedule.service.HomeworkService;
import ru.khanin.dmitrii.schedule.service.LessonService;
import ru.khanin.dmitrii.schedule.service.ScheduleService;
import ru.khanin.dmitrii.schedule.service.TempScheduleService;
import ru.khanin.dmitrii.schedule.service.UserService;
import ru.khanin.dmitrii.schedule.service.jdbc.JdbcFlowService;
import ru.khanin.dmitrii.schedule.service.jdbc.JdbcHomeworkService;
import ru.khanin.dmitrii.schedule.service.jdbc.JdbcLessonService;
import ru.khanin.dmitrii.schedule.service.jdbc.JdbcScheduleService;
import ru.khanin.dmitrii.schedule.service.jdbc.JdbcTempScheduleService;
import ru.khanin.dmitrii.schedule.service.jdbc.JdbcUserService;

@Validated
@Configuration
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
@RequiredArgsConstructor
@Getter
@Setter
public class AppConfig {
	@NotNull
	private URI brsUrl;
	
	@NotNull
	private AccessType databaseAccessType;
	
	@NotNull
	private String cron;
	
	public enum AccessType {
		JDBC
	}

	@Configuration
	@ConditionalOnProperty(prefix="app", name="database-access-type", havingValue = "jdbc")
	public static class JdbcAccessConfiguration {
		@Bean
		public FlowService flowService(
				JdbcFlowRepo flowRepo, JdbcHomeworkRepo homeworkRepo,
				JdbcScheduleRepo scheduleRepo, JdbcTempScheduleRepo tempScheduleRepo,
				JdbcUserRepo userRepo
		) {
			return new JdbcFlowService(flowRepo, homeworkRepo, scheduleRepo, tempScheduleRepo, userRepo);
		}
		
		@Bean
		public HomeworkService homeworkService(JdbcHomeworkRepo homeworkRepo, JdbcFlowRepo flowRepo) {
			return new JdbcHomeworkService(homeworkRepo, flowRepo);
		}
		
		@Bean
		public LessonService lessonService(
				JdbcLessonRepo lessonRepo, JdbcScheduleRepo scheduleRepo, JdbcTempScheduleRepo tempScheduleRepo
		) {
			return new JdbcLessonService(lessonRepo, scheduleRepo, tempScheduleRepo);
		}
		
		@Bean
		public ScheduleService scheduleService(
				JdbcScheduleRepo scheduleRepo, JdbcFlowRepo flowRepo, JdbcLessonRepo lessonRepo
		) {
			return new JdbcScheduleService(scheduleRepo, flowRepo, lessonRepo);
		}
		
		@Bean
		public TempScheduleService tempScheduleService(
				JdbcTempScheduleRepo tempScheduleRepo, JdbcFlowRepo flowRepo, JdbcLessonRepo lessonRepo
		) {
			return new JdbcTempScheduleService(tempScheduleRepo, flowRepo, lessonRepo);
		}
		
		@Bean
		public UserService userService(JdbcUserRepo userRepo, JdbcFlowRepo flowRepo) {
			return new JdbcUserService(userRepo, flowRepo);
		}
	}
	
	@Bean
	public BrsClient brsClient() {
		return new BrsClient(brsUrl);
	}
}
