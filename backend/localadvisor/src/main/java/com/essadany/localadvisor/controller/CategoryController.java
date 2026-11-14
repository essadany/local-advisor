package com.essadany.localadvisor.controller;

import com.essadany.localadvisor.exception.ConflictException;
import com.essadany.localadvisor.exception.ResourceNotFoundException;
import com.essadany.localadvisor.model.Category;
import com.essadany.localadvisor.model.Place;
import com.essadany.localadvisor.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}/places")
    public ResponseEntity<List<Place>> getPlacesByCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category " + id + " not found"))
                .getPlaces());
    }

    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        if (categoryService.getCategoryByName(category.getName()).isPresent()) {
            throw new ConflictException("Category '" + category.getName() + "' already exists");
        }
        Category createdCategory = categoryService.addCategory(category);
        return ResponseEntity.created(URI.create("/api/v1/categories/" + createdCategory.getCategoryId()))
                .body(createdCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        if (categoryService.getCategoryById(id).isEmpty()) {
            throw new ResourceNotFoundException("Category " + id + " not found");
        }
        category.setCategoryId(id);
        return ResponseEntity.ok(categoryService.updateCategory(category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        if (categoryService.getCategoryById(id).isEmpty()) {
            throw new ResourceNotFoundException("Category " + id + " not found");
        }
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllCategories() {
        categoryService.deleteAllCategories();
        return ResponseEntity.noContent().build();
    }


}
