package com.essadany.localadvisor.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PlaceRequest(
        @NotBlank String name,
        String address,
        String city,
        String country,
        String website,
        String phone,
        String zip,
        String description,
        @Min(0) @Max(5) int rating,
        @NotNull Long categoryId
) {
}
