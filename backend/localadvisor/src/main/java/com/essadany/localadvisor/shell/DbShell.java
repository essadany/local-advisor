package com.essadany.localadvisor.shell;

import com.essadany.localadvisor.model.*;
import com.essadany.localadvisor.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class DbShell {

    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final CategoryRepository categoryRepository;
    private final ReviewRepository reviewRepository;
    private final ImageRepository imageRepository;
    private final FavoriteRepository favoriteRepository;

    @ShellMethod("List all users")
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @ShellMethod("Get user by ID")
    public User getUser(@ShellOption Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @ShellMethod("Count users")
    public long countUsers() {
        return userRepository.count();
    }

    @ShellMethod("Delete user by ID")
    public void deleteUser(@ShellOption Long id) {
        userRepository.deleteById(id);
    }

    @ShellMethod("List all places")
    public List<Place> listPlaces() {
        return placeRepository.findAll();
    }

    @ShellMethod("Get place by ID")
    public Place getPlace(@ShellOption Long id) {
        return placeRepository.findById(id).orElseThrow();
    }

    @ShellMethod("Count places")
    public long countPlaces() {
        return placeRepository.count();
    }

    @ShellMethod("Delete place by ID")
    public void deletePlace(@ShellOption Long id) {
        placeRepository.deleteById(id);
    }

    @ShellMethod("List all categories")
    public List<Category> listCategories() {
        return categoryRepository.findAll();
    }

    @ShellMethod("Get category by ID")
    public Category getCategory(@ShellOption Long id) {
        return categoryRepository.findById(id).orElseThrow();
    }

    @ShellMethod("Count categories")
    public long countCategories() {
        return categoryRepository.count();
    }

    @ShellMethod("Delete category by ID")
    public void deleteCategory(@ShellOption Long id) {
        categoryRepository.deleteById(id);
    }

    @ShellMethod("List all reviews")
    public List<Review> listReviews() {
        return reviewRepository.findAll();
    }

    @ShellMethod("Get review by ID")
    public Review getReview(@ShellOption Long id) {
        return reviewRepository.findById(id).orElseThrow();
    }

    @ShellMethod("Count reviews")
    public long countReviews() {
        return reviewRepository.count();
    }

    @ShellMethod("Delete review by ID")
    public void deleteReview(@ShellOption Long id) {
        reviewRepository.deleteById(id);
    }

    @ShellMethod("List all images")
    public List<Image> listImages() {
        return imageRepository.findAll();
    }

    @ShellMethod("Get image by ID")
    public Image getImage(@ShellOption Long id) {
        return imageRepository.findById(id).orElseThrow();
    }

    @ShellMethod("Count images")
    public long countImages() {
        return imageRepository.count();
    }

    @ShellMethod("Delete image by ID")
    public void deleteImage(@ShellOption Long id) {
        imageRepository.deleteById(id);
    }

    @ShellMethod("List all favorites")
    public List<Favorite> listFavorites() {
        return favoriteRepository.findAll();
    }

    @ShellMethod("Get favorite by ID")
    public Favorite getFavorite(@ShellOption Long id) {
        return favoriteRepository.findById(id).orElseThrow();
    }

    @ShellMethod("Count favorites")
    public long countFavorites() {
        return favoriteRepository.count();
    }

    @ShellMethod("Delete favorite by ID")
    public void deleteFavorite(@ShellOption Long id) {
        favoriteRepository.deleteById(id);
    }
}
