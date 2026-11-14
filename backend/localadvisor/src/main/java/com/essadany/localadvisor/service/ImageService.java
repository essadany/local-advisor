package com.essadany.localadvisor.service;

import com.essadany.localadvisor.exception.ResourceNotFoundException;
import com.essadany.localadvisor.model.Image;
import com.essadany.localadvisor.model.Place;
import com.essadany.localadvisor.model.User;
import com.essadany.localadvisor.repository.ImageRepository;
import com.essadany.localadvisor.repository.PlaceRepository;
import com.essadany.localadvisor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private UserRepository userRepository;

    public Image saveImage(Long placeId, Long userId, String imageUrl) {
        Place place = placeRepository.findById(placeId).orElseThrow(() -> new ResourceNotFoundException("Place " + placeId + " not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found"));

        Image image = new Image();
        image.setUrl(imageUrl);
        image.setPlace(place);
        image.setUser(user);

        return imageRepository.save(image);
    }

    public List<Image> getImagesByPlaceId(Long placeId) {
        Place place = placeRepository.findById(placeId).orElseThrow(() -> new ResourceNotFoundException("Place " + placeId + " not found"));
        return (List<Image>) imageRepository.findAllByPlace(place);
    }
    
    public Optional<Image> getImageById(Long imageId) {
        return imageRepository.findById(imageId);
    }

    public void deleteImage(Long imageId) {
        imageRepository.deleteById(imageId);
    }
}