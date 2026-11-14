package com.essadany.localadvisor.dto;

import jakarta.validation.constraints.NotNull;

public record FavoriteRequest(@NotNull Long placeId) {
}
