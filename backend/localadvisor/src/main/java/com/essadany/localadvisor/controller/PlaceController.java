package com.essadany.localadvisor.controller;

import com.essadany.localadvisor.dto.PlaceRequest;
import com.essadany.localadvisor.exception.ResourceNotFoundException;
import com.essadany.localadvisor.model.Place;
import com.essadany.localadvisor.service.PlaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/places")
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService placeService;

    @GetMapping
    public List<Place> getPlaces() {
        return placeService.getAllPlaces();
    }

    @GetMapping("/{id}")
    public Place getPlace(@PathVariable Long id) {
        return placeService.getPlaceById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Place " + id + " not found"));
    }

    @PostMapping
    public ResponseEntity<Place> createPlace(@Valid @RequestBody PlaceRequest request) {
        Place created = placeService.createPlace(toPlace(request), request.categoryId());
        return ResponseEntity.created(URI.create("/api/v1/places/" + created.getPlaceId())).body(created);
    }

    @PutMapping("/{id}")
    public Place updatePlace(@PathVariable Long id, @Valid @RequestBody PlaceRequest request) {
        getPlace(id);
        return placeService.updatePlace(id, toPlace(request), request.categoryId());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlace(@PathVariable Long id) {
        getPlace(id);
        placeService.deletePlace(id);
        return ResponseEntity.noContent().build();
    }

    private Place toPlace(PlaceRequest request) {
        Place place = new Place();
        place.setName(request.name());
        place.setAddress(request.address());
        place.setCity(request.city());
        place.setCountry(request.country());
        place.setWebsite(request.website());
        place.setPhone(request.phone());
        place.setZip(request.zip());
        place.setDescription(request.description());
        place.setRating(request.rating());
        return place;
    }
}
