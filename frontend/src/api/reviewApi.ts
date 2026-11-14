import { useAuth } from "@/composables/useAuth";
import type { User } from "./userApi";
import type { Place } from "./placeApi";

export interface Review {
  reviewId: number;
  user : User;
  place : Place
  rating: number;
  content: string;
  createdAt: string;
}

const { token } = useAuth();

export const reviewApi = {

  getAllReviews: async (): Promise<Review[]> => {
    const response = await fetch('/api/v1/reviews', {
    });
    if (!response.ok) throw new Error('Erreur lors du chargement des Reviews');
    return await response.json();
  },

  getReviewsByPlaceId: async (place_id: number): Promise<Review[]> => {
    const response = await fetch(`/api/v1/reviews/place/${place_id}`, {
      headers: { Authorization: `Bearer ${token.value}` },
    });
    if (!response.ok) throw new Error('Erreur lors du chargement des Reviews');
    return await response.json();
  },

  getReviewsByUserId: async (user_id: number): Promise<Review[]> => {
    const response = await fetch(`/api/v1/reviews/user/${user_id}`, {
      headers: { Authorization: `Bearer ${token.value}` },
    });
    if (!response.ok) throw new Error('Erreur lors du chargement des Reviews');
    return await response.json();
  },

  getReviewById: async (review_id: string): Promise<Review | null> => {
    const response = await fetch(`/api/v1/reviews/${review_id}`);
    if (!response.ok) throw new Error('Erreur lors du chargement du Review');
    return await response.json();
  },

  createReview: async (review: Review): Promise<Review> => {
    const response = await fetch('/api/v1/reviews', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token.value}`
      },
      body: JSON.stringify(review)
    });
    if (!response.ok) throw new Error('Erreur lors de la création du Review');
    return await response.json();
  },

  updateReview: async (review: Review): Promise<Review> => {
    const response = await fetch(`/api/v1/reviews/${review.reviewId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token.value}`
      },
      body: JSON.stringify(review)
    });
    if (!response.ok) throw new Error('Erreur lors de la mise à jour du Review');
    return await response.json();
  },

  deleteReview: async (id: number): Promise<void> => {
    const response = await fetch(`/api/v1/reviews/${id}`, {
      method: 'DELETE',
      headers: { Authorization: `Bearer ${token.value}` },
    });
    if (!response.ok) throw new Error("Erreur lors de la suppression du Review");
  }
}

