package ru.khanin.dmitrii.schedule.client;

import java.net.URI;
import java.util.List;

import org.springframework.web.reactive.function.client.WebClient;

import ru.khanin.dmitrii.schedule.dto.brs.ListStudGroupsResponse;
import ru.khanin.dmitrii.schedule.dto.brs.StudGroupResponse;

public class BrsClient {
	private final WebClient webClient;

	public BrsClient(URI baseUrl) {
		this.webClient = WebClient.builder().baseUrl(baseUrl.toString()).build();
	}

	public List<StudGroupResponse> getStudGroups() {
		ListStudGroupsResponse response = webClient.get().retrieve().bodyToMono(ListStudGroupsResponse.class).block();
		
		return response.stud_groups();
	}
}
