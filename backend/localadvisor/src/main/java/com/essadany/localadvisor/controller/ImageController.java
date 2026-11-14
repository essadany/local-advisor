package com.essadany.localadvisor.controller;

import com.essadany.localadvisor.dto.ImageRequest;
import com.essadany.localadvisor.exception.ResourceNotFoundException;
import com.essadany.localadvisor.model.Image;
import com.essadany.localadvisor.service.ImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @GetMapping
    public List<Image> getImages(
            @RequestParam(required = false) Long placeId,
            @RequestParam(required = false) Long userId) {
        return imageService.getImages(placeId, userId);
    }

    @GetMapping("/{id}")
    public Image getImage(@PathVariable Long id) {
        return imageService.getImageById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image " + id + " not found"));
    }

    @PostMapping
    public ResponseEntity<Image> createImage(@Valid @RequestBody ImageRequest request) {
        Image created = imageService.createImage(request.placeId(), request.userId(), request.imageUrl());
        return ResponseEntity.created(URI.create("/api/v1/images/" + created.getId())).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        getImage(id);
        imageService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}
