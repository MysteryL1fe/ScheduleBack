package ru.khanin.dmitrii.schedule.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.khanin.dmitrii.schedule.dto.user.UserRequest;
import ru.khanin.dmitrii.schedule.dto.user.UserResponse;
import ru.khanin.dmitrii.schedule.entity.User;
import ru.khanin.dmitrii.schedule.exception.NoAccessException;
import ru.khanin.dmitrii.schedule.service.UserService;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
	private final UserService userService;

	@GetMapping("/all")
	public ResponseEntity<List<UserResponse>> getAllUsers(@RequestHeader("api_key") String apiKey) {
		log.info(String.format("Received request from \"%s\" to get all users", apiKey));
		
		if (!userService.checkAdminAccessByApiKey(apiKey))
			throw new NoAccessException("Нет доступа для просмотра пользователей");
		
		log.info(String.format("User \"%s\" is trying to get all users", apiKey));
		
		Collection<User> found = userService.findAll();
		List<UserResponse> result = new ArrayList<>();
		found.forEach((e) -> {
			result.add(new UserResponse(e.getId(), e.getApiKey(), e.getName(), e.getAccess(), e.getFlow()));
		});

		log.info(String.format(
				"User \"%s\" has successfully get all (%s) users: %s", apiKey, found.size(), found
		));
		
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("api_key")
	public ResponseEntity<List<UserResponse>> getAllUsersByApiKey(
			@RequestHeader("api_key") String apiKey, @RequestBody String api_key
	) {
		log.info(String.format("Received request from \"%s\" to get all users by api key %s", apiKey, api_key));
		
		if (!userService.checkAdminAccessByApiKey(apiKey))
			throw new NoAccessException("Нет доступа для просмотра пользователей");
		
		Collection<User> found = userService.findAllByApiKey(api_key);
		List<UserResponse> result = new ArrayList<>();
		found.forEach((e) -> {
			result.add(new UserResponse(e.getId(), e.getApiKey(), e.getName(), e.getAccess(), e.getFlow()));
		});

		log.info(String.format(
				"User \"%s\" has successfully get all (%s) users by api key %s: %s",
				apiKey, found.size(), api_key, found
		));
		
		return ResponseEntity.ok(result);
	}
	
	@PostMapping("/user")
	public ResponseEntity<?> addUser(@RequestHeader("api_key") String apiKey, @RequestBody UserRequest user) {
		log.info(String.format("Received request from \"%s\" to add user %s", apiKey, user));
		
		if (!userService.checkAdminAccessByApiKey(apiKey))
			throw new NoAccessException("Нет доступа для добавления пользователя");

		log.info(String.format("User \"%s\" is trying to add user %s", apiKey, user));
		
		User addedUser = userService.add(
				user.api_key(), user.name(), user.access(),
				user.flow().flow_lvl(), user.flow().course(), user.flow().flow(), user.flow().subgroup()
		);
		
		log.info(String.format("User \"%s\" has successfully added user %s", apiKey, addedUser));
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/all")
	public ResponseEntity<?> deleteAllUsers(@RequestHeader("api_key") String apiKey) {
		log.info(String.format("Received request from \"%s\" to delete all users", apiKey));
		
		if (!userService.checkAdminAccessByApiKey(apiKey))
			throw new NoAccessException("Нет доступа для удаления пользователей");
		
		log.info(String.format("User \"%s\" is trying to delete all users", apiKey));
		
		Collection<User> deletedUsers = userService.deleteAll();

		log.info(String.format(
				"User \"%s\" has successfully deleted all (%s) users: %s",
				apiKey, deletedUsers.size(), deletedUsers
		));
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/api_key")
	public ResponseEntity<?> deleteAllUsersByApiKey(
			@RequestHeader("api_key") String apiKey, @RequestBody String api_key
	) {
		log.info(String.format("Received request from \"%s\" to delete all users by api key %s", apiKey, api_key));
		
		if (!userService.checkAdminAccessByApiKey(apiKey))
			throw new NoAccessException("Нет доступа для удаления пользователей");
		
		log.info(String.format("User \"%s\" is trying to delete all users by api key", apiKey, api_key));
		
		Collection<User> deletedUsers = userService.deleteAllByApiKey(api_key);

		log.info(String.format(
				"User \"%s\" has successfully deleted all (%s) users by api key %s: %s",
				apiKey, deletedUsers.size(), api_key, deletedUsers
		));
		
		return ResponseEntity.ok().build();
	}
}
