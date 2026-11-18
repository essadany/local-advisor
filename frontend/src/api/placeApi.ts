// Fonctions API pour les Places

import { useAuth } from "@/composables/useAuth";
import type { Category } from "./categoryApi";

export interface Place {
  placeId: number;
  name: string;
  address: string;
  city: string;
  country: string | null;
  website: string | null;
  phone: string | null;
  zip: string;
  description: string;
  rating: number;
  category: Category;
}

export interface CreatePlacePayload {
  name: string;
  description: string;
  address: string;
  city: string;
  zip: string;
  country: string | null;
  website: string | null;
  phone: string | null;
  categoryId: number;
}

const { token } = useAuth();

const authHeaders = () => ({
  'Content-Type': 'application/json',
  'Authorization': `Bearer ${token.value}`
});

export const placeApi = {
  getPlaces: async (): Promise<Place[]> => {
    const response = await fetch('/api/v1/places');
    if (!response.ok) throw new Error('Erreur lors du chargement des Places');
    return await response.json();
  },

  getPlaceById: async (id: string): Promise<Place | null> => {
    const response = await fetch(`/api/v1/places/${id}`);
    if (!response.ok) throw new Error('Erreur lors du chargement du Place');
    return await response.json();
  },

  createPlace: async (place: CreatePlacePayload): Promise<Place> => {
    const response = await fetch('/api/v1/places', {
      method: 'POST',
      headers: authHeaders(),
      body: JSON.stringify(place)
    });
    if (!response.ok) throw new Error('Erreur lors de la création du Place');
    return await response.json();
  },

  updatePlace: async (place: Place): Promise<Place> => {
    const response = await fetch(`/api/v1/places/${place.placeId}`, {
      method: 'PUT',
      headers: authHeaders(),
      body: JSON.stringify({ ...place, categoryId: place.category?.categoryId })
    });
    if (!response.ok) throw new Error('Erreur lors de la mise à jour du Place');
    return await response.json();
  },

  deletePlace: async (id: number): Promise<void> => {
    const response = await fetch(`/api/v1/places/${id}`, {
      method: 'DELETE',
      headers: { Authorization: `Bearer ${token.value}` }
    });
    if (!response.ok) throw new Error('Erreur lors de la suppression du Place');
  }
};
