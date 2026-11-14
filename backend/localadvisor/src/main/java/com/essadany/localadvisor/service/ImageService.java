package com.essadany.localadvisor.service;

import com.essadany.localadvisor.exception.ResourceNotFoundException;
import com.essadany.localadvisor.model.Image;
import com.essadany.localadvisor.model.Place;
import com.essadany.localadvisor.model.User;
import com.essadany.localadvisor.repository.ImageRepository;
import com.essadany.localadvisor.repository.PlaceRepository;
import com.essadany.localadvisor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    public Image createImage(Long placeId, Long userId, String imageUrl) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new ResourceNotFoundException("Place " + placeId + " not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found"));

        Image image = new Image();
        image.setUrl(imageUrl);
        image.setPlace(place);
        image.setUser(user);

        return imageRepository.save(image);
    }

    @Transactional(readOnly = true)
    public List<Image> getImages(Long placeId, Long userId) {
        if (placeId != null) {
            Place place = placeRepository.findById(placeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Place " + placeId + " not found"));
            return toList(imageRepository.findAllByPlace(place));
        }
        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found"));
            return toList(imageRepository.findAllByUser(user));
        }
        return imageRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Image> getImageById(Long imageId) {
        return imageRepository.findById(imageId);
    }

    public void deleteImage(Long imageId) {
        imageRepository.deleteById(imageId);
    }

    private List<Image> toList(Iterable<Image> images) {
        java.util.ArrayList<Image> result = new java.util.ArrayList<>();
        images.forEach(result::add);
        return result;
    }
}
