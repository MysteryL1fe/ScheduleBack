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
import ru.khanin.dmitrii.schedule.dto.cabinet.CabinetRequest;
import ru.khanin.dmitrii.schedule.dto.cabinet.CabinetResponse;
import ru.khanin.dmitrii.schedule.dto.cabinet.DeleteCabinetRequest;
import ru.khanin.dmitrii.schedule.entity.Cabinet;
import ru.khanin.dmitrii.schedule.service.CabinetService;

@RestController
@RequestMapping("/cabinet")
@RequiredArgsConstructor
@Slf4j
public class CabinetController {
	private final CabinetService cabinetService;
	
	@GetMapping("/cabinet")
	public ResponseEntity<CabinetResponse> getCabinet(
			@RequestParam String cabinet, @RequestParam String building
	) {
		log.trace(String.format("Received request to get cabinet (cabinet: %s, building: %s)", cabinet, building));
		
		Cabinet foundCabinet = cabinetService.findByCabinetAndBuilding(cabinet, building);
		CabinetResponse response = convertCabinetToResponse(foundCabinet);
		
		log.trace(String.format("Found cabinet: %s", foundCabinet));
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<CabinetResponse>> getAllCabinets() {
		log.trace("Received request to get all cabinets");
		
		Collection<Cabinet> found = cabinetService.findAll();
		List<CabinetResponse> result = new ArrayList<>();
		found.forEach(e -> result.add(convertCabinetToResponse(e)));
		
		log.trace(String.format("Found all (%s) cabinets: %s", found.size(), found));
		
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/auditory")
	public ResponseEntity<List<CabinetResponse>> getAllCabinetsByCabinet(@RequestParam String cabinet) {
		log.trace(String.format("Received request to get all cabinets by cabinet %s", cabinet));
		
		Collection<Cabinet> found = cabinetService.findAllByCabinet(cabinet);
		List<CabinetResponse> result = new ArrayList<>();
		found.forEach(e -> result.add(convertCabinetToResponse(e)));
		
		log.trace(String.format("Found all (%s) cabinets by cabinet %s: %s", found.size(), cabinet, found));
		
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/building")
	public ResponseEntity<List<CabinetResponse>> getAllCabinetsByBuilding(@RequestParam String building) {
		log.trace(String.format("Received request to get all cabinets by building %s", building));
		
		Collection<Cabinet> found = cabinetService.findAllByBuilding(building);
		List<CabinetResponse> result = new ArrayList<>();
		found.forEach(e -> result.add(convertCabinetToResponse(e)));
		
		log.trace(String.format("Found all (%s) cabinets by building %s: %s", found.size(), building, found));
		
		return ResponseEntity.ok(result);
	}
	
	@PostMapping("/cabinet")
	public ResponseEntity<?> addCabinet(@RequestBody CabinetRequest cabinet) {
		log.trace(String.format("Received request to add cabinet %s", cabinet));
		
		Cabinet addedCabinet = cabinetService.addOrUpdate(cabinet.cabinet(), cabinet.building(), cabinet.address());
		
		log.trace(String.format("Successfully added cabinet %s", addedCabinet));
		
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/cabinet")
	public ResponseEntity<?> deleteCabinet(@RequestBody DeleteCabinetRequest cabinet) {
		log.trace(String.format("Received request to delete cabinet %s", cabinet));
		
		Cabinet deleted = cabinetService.deleteByCabinetAndBuilding(cabinet.cabinet(), cabinet.building());
		
		log.trace(String.format("Successfully deleted cabinet %s", deleted));
		
		return ResponseEntity.ok().build();
	}
	
	private CabinetResponse convertCabinetToResponse(Cabinet cabinet) {
		return new CabinetResponse(cabinet.getCabinet(), cabinet.getBuilding(), cabinet.getAddress());
	}
}
