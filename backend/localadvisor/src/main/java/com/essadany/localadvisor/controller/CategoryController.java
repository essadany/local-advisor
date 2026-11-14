package com.essadany.localadvisor.controller;

import com.essadany.localadvisor.model.Category;
import com.essadany.localadvisor.model.Place;
import com.essadany.localadvisor.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        if (categoryService.getAllCategories().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}/places")
    public ResponseEntity<List<Place>> getPlacesByCategory(@PathVariable Long id) {
        if (categoryService.getCategoryById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categoryService.getCategoryById(id).get().getPlaces());
    }

    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        if (categoryService.getCategoryByName(category.getName()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Category createdCategory = categoryService.addCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        if (categoryService.getCategoryById(id).isEmpty()) {
            throw new RuntimeException("Category not found");
        }
        category.setCategoryId(id);
        return ResponseEntity.ok(categoryService.updateCategory(category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllCategories() {
        categoryService.deleteAllCategories();
        return ResponseEntity.noContent().build();
    }


}
