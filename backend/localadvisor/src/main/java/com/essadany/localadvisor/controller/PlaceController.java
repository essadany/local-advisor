package com.essadany.localadvisor.controller;

import com.essadany.localadvisor.model.Place;
import com.essadany.localadvisor.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/places")
class PlaceController {
    @Autowired
    private PlaceService placeService;

    @GetMapping
    public ResponseEntity<List<Place>> getAllPlaces() {
        if (placeService.getAllPlaces().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(placeService.getAllPlaces());
    }

    @PostMapping
    public ResponseEntity<Place> addPlace(@RequestBody Place place) {
        Place createdPlace = placeService.addPlace(place);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlace);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Place> updatePlace(@PathVariable Long id, @RequestBody Place place) {
        if (placeService.getPlaceById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        place.setPlaceId(id);
        return ResponseEntity.ok(placeService.updatePlace(place));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlace(@PathVariable Long id) {
        placeService.deletePlace(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllPlaces() {
        placeService.deleteAllPlaces();
        return ResponseEntity.noContent().build();
    }




}