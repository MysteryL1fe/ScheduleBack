package ru.khanin.dmitrii.schedule.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.khanin.dmitrii.schedule.dto.flow.FlowResponse;
import ru.khanin.dmitrii.schedule.dto.user.AddUserRequest;
import ru.khanin.dmitrii.schedule.dto.user.UserFlowRequest;
import ru.khanin.dmitrii.schedule.dto.user.UserRequest;
import ru.khanin.dmitrii.schedule.dto.user.UserResponse;
import ru.khanin.dmitrii.schedule.entity.User;
import ru.khanin.dmitrii.schedule.entity.UserFlow;
import ru.khanin.dmitrii.schedule.service.UserFlowService;
import ru.khanin.dmitrii.schedule.service.UserService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
	private final UserService userService;
	private final UserFlowService userFlowService;
	
	@GetMapping("/login")
	public ResponseEntity<UserResponse> getUserByLogin(@RequestParam String login) {
		log.trace(String.format("Received request to get user by login %s", login));
		
		User found = userService.findByLogin(login);
		UserResponse response = convertUserToResponse(found);
		
		log.trace(String.format("Found user: %s", response));
		
		return ResponseEntity.ok(response);
	}

	@GetMapping("/all")
	public ResponseEntity<List<UserResponse>> getAllUsers() {
		log.trace(String.format("Received request to get all users"));
		
		Collection<User> found = userService.findAll();
		List<UserResponse> result = new ArrayList<>();
		found.forEach(e -> result.add(convertUserToResponse(e)));

		log.trace(String.format("Successfully found all (%s) users: %s", found.size(), found));
		
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/flow")
	public ResponseEntity<List<UserResponse>> getAllUsersByFlow(
			@RequestParam(name = "education_level") int educationLevel, @RequestParam int course,
			@RequestParam int group, @RequestParam int subgroup
	) {
		log.trace(String.format(
				"Received request to get all users by flow %s.%s.%s.%s", educationLevel, course, group, subgroup
		));
		
		Collection<UserFlow> found = userFlowService.findAllByFlow(educationLevel, course, group, subgroup);
		List<UserResponse> result = new ArrayList<>();
		found.forEach(e -> result.add(convertUserFlowToUserResponse(e)));
		
		log.trace(String.format(
				"Successfully found all (%s) users by flow %s.%s.%s.%s: %s",
				found.size(), educationLevel, course, group, subgroup, found
		));
		
		return ResponseEntity.ok(result);
	}
	
	@PostMapping("/user")
	public ResponseEntity<?> addUser(@RequestBody AddUserRequest user) {
		log.trace(String.format("Received request to add user %s", user));
		
		User addedUser = userService.addOrUpdate(
				user.login(), user.password(), user.admin()
		);
		
		log.trace(String.format("Successfully added user %s", addedUser));
		
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/flow")
	public ResponseEntity<?> addFlowToUser(@RequestBody UserFlowRequest userFlow) {
		log.trace(String.format(
				"Received request to add flow %s.%s.%s.%s to user %s",
				userFlow.flow().education_level(), userFlow.flow().course(),
				userFlow.flow().group(), userFlow.flow().subgroup(), userFlow.user().login()
		));
		
		UserFlow addedUserFlow = userFlowService.add(
				userFlow.user().login(), userFlow.flow().education_level(), userFlow.flow().course(),
				userFlow.flow().group(), userFlow.flow().subgroup()
		);
		
		log.trace(String.format("Succesfully added user_flow %s", addedUserFlow));
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/user")
	public ResponseEntity<?> deleteUser(@RequestBody UserRequest user) {
		log.trace(String.format("Received request to delete user %s", user.login()));
		
		User deleted = userService.deleteByLogin(user.login());
		
		log.trace(String.format("Succesfully deleted user %s", deleted));
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/flow")
	public ResponseEntity<?> deleteFlowFromUser(@RequestBody UserFlowRequest userFlow) {
		log.trace(String.format(
				"Received request to delete flow %s.%s.%s.%s from user %s",
				userFlow.flow().education_level(), userFlow.flow().course(),
				userFlow.flow().group(), userFlow.flow().subgroup(), userFlow.user().login()
		));
		
		UserFlow deleted = userFlowService.deleteByUserAndFlow(
				userFlow.user().login(), userFlow.flow().education_level(), userFlow.flow().course(),
				userFlow.flow().group(), userFlow.flow().subgroup()
		);
		
		log.trace(String.format("Successfully deleted user_flow %s", deleted));
		
		return ResponseEntity.ok().build();
	}
	
	private UserResponse convertUserToResponse(User user) {
		List<FlowResponse> flows = new ArrayList<>();
		user.getFlows().forEach(e -> flows.add(
				new FlowResponse(
					e.getEducationLevel(), e.getCourse(),
					e.getGroup(), e.getSubgroup(),
					e.getLastEdit(), e.getLessonsStartDate(),
					e.getSessionStartDate(), e.getSessionEndDate(),
					e.getActive()
				)
		));
		
		return new UserResponse(user.getLogin(), user.getAdmin(), flows);
	}
	
	private UserResponse convertUserFlowToUserResponse(UserFlow userFlow) {
		List<FlowResponse> flows = new ArrayList<>();
		userFlow.getUser().getFlows().forEach(e -> flows.add(
				new FlowResponse(
					e.getEducationLevel(), e.getCourse(),
					e.getGroup(), e.getSubgroup(),
					e.getLastEdit(), e.getLessonsStartDate(),
					e.getSessionStartDate(), e.getSessionEndDate(),
					e.getActive()
				)
		));
		
		return new UserResponse(
				userFlow.getUser().getLogin(), userFlow.getUser().getAdmin(), flows
		);
	}
}
