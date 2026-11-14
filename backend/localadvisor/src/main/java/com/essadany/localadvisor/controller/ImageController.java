package com.essadany.localadvisor.controller;

import com.essadany.localadvisor.model.Image;
import com.essadany.localadvisor.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping("/place/{placeId}")
    public ResponseEntity<List<Image>> getImagesByPlace(@PathVariable Long placeId) {
        List<Image> images = imageService.getImagesByPlaceId(placeId);
        return ResponseEntity.ok(images);
    }

    @PostMapping
    public ResponseEntity<Image> addImage(@RequestParam Long placeId, @RequestParam Long userId, @RequestParam String imageUrl) {
        Image image = imageService.saveImage(placeId, userId, imageUrl);
        return ResponseEntity.ok(image);
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long imageId) {
        imageService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }
}