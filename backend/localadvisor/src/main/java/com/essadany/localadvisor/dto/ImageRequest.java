package com.essadany.localadvisor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ImageRequest(
        @NotNull Long placeId,
        @NotNull Long userId,
        @NotBlank String imageUrl
) {
}
