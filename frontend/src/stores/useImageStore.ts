// stores/useImageStore.ts
import { defineStore } from "pinia";
import { addImage, deleteImage, fetchImagesByPlaceId } from "@/api/ImageApi";

export const useImageStore = defineStore("imageStore", {
  state: () => ({
    isLoading: false,
    errorMessage: null as string | null,
  }),

  getters: {
    hasError: (state) => !!state.errorMessage,
  },

  actions: {
    async addPlaceImage(imageUrl: string, place_id: number, user_id: number) {
      try {
        this.isLoading = true;
        this.errorMessage = null;
        const data = await addImage(imageUrl, place_id, user_id);
        return data;
      } catch (error) {
        this.errorMessage = (error as Error).message;
      } finally {
        this.isLoading = false;
      }
    },

    async getPlaceImages(place_id: number) {
      try {
        this.isLoading = true;
        this.errorMessage = null;
        const data = await fetchImagesByPlaceId(place_id);
        return data;
      } catch (error) {
        this.errorMessage = (error as Error).message;
      } finally {
        this.isLoading = false;
      }
    },

    async deletePlaceImage(imageId: number) {
      try {
        this.isLoading = true;
        this.errorMessage = null;
        await deleteImage(imageId);
      } catch (error) {
        this.errorMessage = (error as Error).message;
      } finally {
        this.isLoading = false;
      }
    },
  },
});
