package ru.khanin.dmitrii.schedule.configuration;

import java.net.URI;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.khanin.dmitrii.schedule.client.BrsClient;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcCabinetRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcFlowRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcHomeworkRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcScheduleRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcSubjectRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcTeacherRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcTempScheduleRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcUserFlowRepo;
import ru.khanin.dmitrii.schedule.repo.jdbc.JdbcUserRepo;
import ru.khanin.dmitrii.schedule.service.CabinetService;
import ru.khanin.dmitrii.schedule.service.FlowService;
import ru.khanin.dmitrii.schedule.service.HomeworkService;
import ru.khanin.dmitrii.schedule.service.ScheduleService;
import ru.khanin.dmitrii.schedule.service.SubjectService;
import ru.khanin.dmitrii.schedule.service.TeacherService;
import ru.khanin.dmitrii.schedule.service.TempScheduleService;
import ru.khanin.dmitrii.schedule.service.UserFlowService;
import ru.khanin.dmitrii.schedule.service.UserService;
import ru.khanin.dmitrii.schedule.service.jdbc.JdbcCabinetService;
import ru.khanin.dmitrii.schedule.service.jdbc.JdbcFlowService;
import ru.khanin.dmitrii.schedule.service.jdbc.JdbcHomeworkService;
import ru.khanin.dmitrii.schedule.service.jdbc.JdbcScheduleService;
import ru.khanin.dmitrii.schedule.service.jdbc.JdbcSubjectService;
import ru.khanin.dmitrii.schedule.service.jdbc.JdbcTeacherService;
import ru.khanin.dmitrii.schedule.service.jdbc.JdbcTempScheduleService;
import ru.khanin.dmitrii.schedule.service.jdbc.JdbcUserFlowService;
import ru.khanin.dmitrii.schedule.service.jdbc.JdbcUserService;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
@RequiredArgsConstructor
@Getter
@Setter
public class AppConfig {
	@NonNull
	private URI brsUrl;
	
	@NonNull
	private AccessType databaseAccessType;
	
	@NonNull
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
				JdbcUserFlowRepo userFlowRepo
		) {
			return new JdbcFlowService(flowRepo, homeworkRepo, scheduleRepo, tempScheduleRepo, userFlowRepo);
		}
		
		@Bean
		public HomeworkService homeworkService(
				JdbcHomeworkRepo homeworkRepo, JdbcFlowRepo flowRepo, JdbcSubjectRepo subjectRepo
		) {
			return new JdbcHomeworkService(homeworkRepo, flowRepo, subjectRepo);
		}
		
		@Bean
		public ScheduleService scheduleService(
				JdbcScheduleRepo scheduleRepo, JdbcFlowRepo flowRepo, JdbcSubjectRepo subjectRepo,
				JdbcTeacherRepo teacherRepo, JdbcCabinetRepo cabinetRepo
		) {
			return new JdbcScheduleService(scheduleRepo, flowRepo, subjectRepo, teacherRepo, cabinetRepo);
		}
		
		@Bean
		public TempScheduleService tempScheduleService(
				JdbcTempScheduleRepo tempScheduleRepo, JdbcFlowRepo flowRepo, JdbcSubjectRepo subjectRepo,
				JdbcTeacherRepo teacherRepo, JdbcCabinetRepo cabinetRepo
		) {
			return new JdbcTempScheduleService(tempScheduleRepo, flowRepo, subjectRepo, teacherRepo, cabinetRepo);
		}
		
		@Bean
		public UserService userService(JdbcUserRepo userRepo, JdbcUserFlowRepo userFlowRepo) {
			return new JdbcUserService(userRepo, userFlowRepo);
		}
		
		@Bean
		public CabinetService cabinetService(
				JdbcCabinetRepo cabinetRepo, JdbcScheduleRepo scheduleRepo, JdbcTempScheduleRepo tempScheduleRepo
		) {
			return new JdbcCabinetService(cabinetRepo, scheduleRepo, tempScheduleRepo);
		}
		
		@Bean
		public SubjectService subjectService(
				JdbcSubjectRepo subjectRepo, JdbcScheduleRepo scheduleRepo, JdbcTempScheduleRepo tempScheduleRepo,
				JdbcHomeworkRepo homeworkRepo
		) {
			return new JdbcSubjectService(subjectRepo, scheduleRepo, tempScheduleRepo, homeworkRepo);
		}
		
		@Bean
		public TeacherService teacherService(
				JdbcTeacherRepo teacherRepo, JdbcScheduleRepo scheduleRepo, JdbcTempScheduleRepo tempScheduleRepo
		) {
			return new JdbcTeacherService(teacherRepo, scheduleRepo, tempScheduleRepo);
		}
		
		@Bean
		public UserFlowService userFlowService(
				JdbcUserFlowRepo userFlowRepo, JdbcUserRepo userRepo, JdbcFlowRepo flowRepo
		) {
			return new JdbcUserFlowService(userFlowRepo, userRepo, flowRepo);
		}
	}
	
	@Bean
	public BrsClient brsClient() {
		return new BrsClient(brsUrl);
	}
}
