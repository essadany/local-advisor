package com.essadany.localadvisor.service;

import com.essadany.localadvisor.exception.ResourceNotFoundException;
import com.essadany.localadvisor.model.Category;
import com.essadany.localadvisor.model.Place;
import com.essadany.localadvisor.repository.CategoryRepository;
import com.essadany.localadvisor.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final CategoryRepository categoryRepository;

    public Place addPlace(Place place) {
        Long categoryId = place.getCategory().getCategoryId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category " + categoryId + " not found"));
        place.setCategory(category);
        return placeRepository.save(place);
    }

    @Transactional(readOnly = true)
    public List<Place> getAllPlaces() {
        return placeRepository.findAll();
    }

    public Place updatePlace(Place place) {
        return placeRepository.save(place);
    }

    public void deletePlace(Long id) {
        placeRepository.deleteById(id);
    }

    public void deleteAllPlaces() {
        placeRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    public Optional<Place> getPlaceById(Long placeId) {
        return placeRepository.findById(placeId);
    }
}
