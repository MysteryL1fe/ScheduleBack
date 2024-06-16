package ru.khanin.dmitrii.schedule.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.khanin.dmitrii.schedule.exception.NoAccessException;
import ru.khanin.dmitrii.schedule.service.BrsScheduler;
import ru.khanin.dmitrii.schedule.service.UserService;

@RestController
@RequestMapping("/util")
@RequiredArgsConstructor
@Slf4j
public class UtilController {
	private final BrsScheduler brsScheduler;
	private final UserService userService;
	
	@GetMapping("/groups")
	public ResponseEntity<?> checkStudGroups(@RequestHeader("api_key") String apiKey) {
		log.info(String.format("Received request from \"%s\" to update stud groups", apiKey));
		
		if (!userService.checkAdminAccessByApiKey(apiKey))
			throw new NoAccessException("Нет доступа для обновления групп");
		
		log.info(String.format("User \"%s\" is trying to update stud groups", apiKey));
		
		brsScheduler.checkStudGroups();

		log.info(String.format("User \"%s\" has successfully updated stud groups", apiKey));
		
		return ResponseEntity.ok().build();
	}
}
