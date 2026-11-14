package com.essadany.localadvisor.controller;

import com.essadany.localadvisor.dto.PlaceRequest;
import com.essadany.localadvisor.exception.ResourceNotFoundException;
import com.essadany.localadvisor.model.Category;
import com.essadany.localadvisor.model.Place;
import com.essadany.localadvisor.service.PlaceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/places")
class PlaceController {
    @Autowired
    private PlaceService placeService;

    @GetMapping
    public ResponseEntity<List<Place>> getAllPlaces() {
        return ResponseEntity.ok(placeService.getAllPlaces());
    }

    @PostMapping
    public ResponseEntity<Place> addPlace(@Valid @RequestBody PlaceRequest request) {
        Place createdPlace = placeService.addPlace(toPlace(request));
        return ResponseEntity.created(URI.create("/api/v1/places/" + createdPlace.getPlaceId()))
                .body(createdPlace);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Place> updatePlace(@PathVariable Long id, @Valid @RequestBody PlaceRequest request) {
        if (placeService.getPlaceById(id).isEmpty()) {
            throw new ResourceNotFoundException("Place " + id + " not found");
        }
        Place place = toPlace(request);
        place.setPlaceId(id);
        return ResponseEntity.ok(placeService.updatePlace(place));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlace(@PathVariable Long id) {
        if (placeService.getPlaceById(id).isEmpty()) {
            throw new ResourceNotFoundException("Place " + id + " not found");
        }
        placeService.deletePlace(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllPlaces() {
        placeService.deleteAllPlaces();
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

        Category category = new Category();
        category.setCategoryId(request.categoryId());
        place.setCategory(category);
        return place;
    }
}
