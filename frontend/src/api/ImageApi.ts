import { useAuth } from '@/composables/useAuth'

export interface Image {
  id: number
  place_id: number
  user_id: number
  url: string
}

const { token } = useAuth()

export const addImage = async (File: File, place_id: number, user_id: number): Promise<Image> => {
  const formData = new FormData()
  formData.append('image', File)
  formData.append('place_id', place_id.toString())
  formData.append('user_id', user_id.toString())
  const response = await fetch('/api/v1/images', {
    method: 'POST',
    headers: {
      Authorization: `Bearer ${token.value}`,
    },
    body: formData,
  })
  if (!response.ok) throw new Error("Erreur lors de la création de l'image")
  return await response.json()
}

export const fetchImagesByPlaceId = async (place_id: number): Promise<Image[]> => {
  const response = await fetch(`/api/v1/images/place/${place_id}`, {
    headers: { Authorization: `Bearer ${token.value}` },
  })
  if (!response.ok) throw new Error('Erreur lors du chargement des images')
  return await response.json()
}

export const fetchImagesByUserId = async (user_id: number): Promise<Image[]> => {
  const response = await fetch(`/api/v1/images/user/${user_id}`, {
    headers: { Authorization: `Bearer ${token.value}` },
  })
  if (!response.ok) throw new Error('Erreur lors du chargement des images')
  return await response.json()
}

export const fetchImageById = async (id: number): Promise<Image | null> => {
  const response = await fetch(`/api/v1/images/${id}`)
  if (!response.ok) throw new Error("Erreur lors du chargement de l'image")
  return await response.json()
}

export const deleteImage = async (id: number): Promise<void> => {
  const response = await fetch(`/api/v1/images/${id}`, {
    method: 'DELETE',
    headers: { Authorization: `Bearer ${token.value}` },
  })
  if (!response.ok) throw new Error("Erreur lors de la suppression de l'image")
}
