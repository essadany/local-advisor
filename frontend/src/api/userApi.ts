import { useAuth } from '@/composables/useAuth'
import type { Place } from './placeApi'

export interface User {
  id: number
  email: string
  password: string
  name: string
  role: string
}

const { token } = useAuth()

export const userApi = {
  getUserById: async (id: number): Promise<User> => {
    const response = await fetch(`/api/v1/users/${id}`, {
      headers: { Authorization: `Bearer ${token.value}` },
    })
    if (!response.ok) throw new Error("Erreur lors du chargement de l'utilisateur")
    return await response.json()
  },

  getUserByEmail: async (email: string): Promise<User> => {
    const response = await fetch(`/api/v1/users/email/${email}`, {
      headers: { Authorization: `Bearer ${token.value}` },
    })
    if (!response.ok) throw new Error("Erreur lors du chargement de l'utilisateur")
    return await response.json()
  },

  getUsers: async (): Promise<User[]> => {
    const response = await fetch('/api/v1/users', {
      headers: { Authorization: `Bearer ${token.value}` },
    })
    if (!response.ok) throw new Error('Erreur lors du chargement des utilisateurs')
    return await response.json()
  },

  updateUser: async (user: User): Promise<User> => {
    const response = await fetch(`/api/v1/users/${user.id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token.value}`,
      },
      body: JSON.stringify(user),
    })
    if (!response.ok) throw new Error("Erreur lors de la mise à jour de l'utilisateur")
    return await response.json()
  },

  deleteUser: async (id: number): Promise<void> => {
    const response = await fetch(`/api/v1/users/${id}`, {
      method: 'DELETE',
      headers: { Authorization: `Bearer ${token.value}` },
    })
    if (!response.ok) throw new Error("Erreur lors de la suppression de l'utilisateur")
  },

  fetchFavorites: async (user_id: number): Promise<Place[]> => {
    const response = await fetch(`/api/v1/favorites/${user_id}`, {
      headers: { Authorization: `Bearer ${token.value}` },
    })
    if (!response.ok) throw new Error('Erreur lors du chargement des Favorites')
    return await response.json()
  },

  createFavorite: async (user_id: number, place_id: number): Promise<void> => {
    const response = await fetch(`/api/v1/favorites/${user_id}/add/${place_id}`, {
      method: 'POST',
      headers: { Authorization: `Bearer ${token.value}` },
    })
    if (!response.ok) throw new Error('Erreur lors de la création du Favorite')
  },

  deleteFavorite: async (id: number): Promise<void> => {
    const response = await fetch(`/api/v1/favorites/${id}`, {
      method: 'DELETE',
      headers: { Authorization: `Bearer ${token.value}` },
    })
    if (!response.ok) throw new Error('Erreur lors de la suppression du Favorite')
  }
}
