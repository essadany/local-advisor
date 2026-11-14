package com.essadany.localadvisor.controller;

import com.essadany.localadvisor.exception.ConflictException;
import com.essadany.localadvisor.exception.ResourceNotFoundException;
import com.essadany.localadvisor.model.Category;
import com.essadany.localadvisor.model.Place;
import com.essadany.localadvisor.service.CategoryService;
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
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<Category> getCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public Category getCategory(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category " + id + " not found"));
    }

    @GetMapping("/{id}/places")
    public List<Place> getCategoryPlaces(@PathVariable Long id) {
        return categoryService.getPlacesByCategoryId(id);
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        if (categoryService.getCategoryByName(category.getName()).isPresent()) {
            throw new ConflictException("Category '" + category.getName() + "' already exists");
        }
        Category created = categoryService.createCategory(category);
        return ResponseEntity.created(URI.create("/api/v1/categories/" + created.getCategoryId())).body(created);
    }

    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestBody Category category) {
        getCategory(id);
        return categoryService.updateCategory(id, category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        getCategory(id);
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
