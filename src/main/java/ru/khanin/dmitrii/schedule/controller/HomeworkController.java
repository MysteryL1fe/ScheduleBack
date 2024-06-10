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
import ru.khanin.dmitrii.schedule.dto.flow.FlowResponse;
import ru.khanin.dmitrii.schedule.dto.homework.DeleteHomeworkRequest;
import ru.khanin.dmitrii.schedule.dto.homework.HomeworkRequest;
import ru.khanin.dmitrii.schedule.dto.homework.HomeworkResponse;
import ru.khanin.dmitrii.schedule.entity.Flow;
import ru.khanin.dmitrii.schedule.entity.Homework;
import ru.khanin.dmitrii.schedule.service.FlowService;
import ru.khanin.dmitrii.schedule.service.HomeworkService;

@RestController
@RequestMapping("homework")
@RequiredArgsConstructor
public class HomeworkController {
	private final HomeworkService homeworkService;
	private final FlowService flowService;
	
	@GetMapping("/all")
	public ResponseEntity<List<HomeworkResponse>> getAllHomeworks() {
		Collection<Homework> found = homeworkService.findAll();
		List<HomeworkResponse> result = new ArrayList<>();
		found.forEach((e) -> {
			Flow flow = flowService.findById(e.getFlow());
			FlowResponse flowResponse = new FlowResponse(
					flow.getFlowLvl(), flow.getCourse(), flow.getFlow(), flow.getSubgroup()
			);
					
			result.add(new HomeworkResponse(
					e.getHomework(), e.getLessonDate(), e.getLessonNum(), flowResponse, e.getLessonName()
			));
		});
		return ResponseEntity.ok(result);
	}
	
	@PostMapping("/homework")
	public ResponseEntity<?> addHomework(@RequestBody HomeworkRequest homework) {
		homeworkService.add(
				homework.homework(), homework.lesson_date(), homework.lesson_num(), homework.flow().flow_lvl(),
				homework.flow().course(), homework.flow().flow(), homework.flow().subgroup(), homework.lesson_name()
		);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/homework")
	public ResponseEntity<?> deleteHomework(@RequestBody DeleteHomeworkRequest homework) {
		homeworkService.delete(
				homework.flow().flow_lvl(), homework.flow().course(), homework.flow().flow(),
				homework.flow().subgroup(), homework.lesson_date(), homework.lesson_num()
		);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/homeworks")
	@Transactional
	public ResponseEntity<?> deleteHomeworks(@RequestBody List<DeleteHomeworkRequest> homeworks) {
		for (DeleteHomeworkRequest homework : homeworks) {
			homeworkService.delete(
					homework.flow().flow_lvl(), homework.flow().course(), homework.flow().flow(),
					homework.flow().subgroup(), homework.lesson_date(), homework.lesson_num()
			);
		}
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/all")
	public ResponseEntity<?> deleteAllHomeworks() {
		homeworkService.deleteAll();
		return ResponseEntity.ok().build();
	}
}
