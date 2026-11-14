import { defineStore } from "pinia";
import { ref } from "vue";
import { userApi, type User } from "@/api/userApi";
import { placeApi, type Place } from "@/api/placeApi";
import { useAuth } from "@/composables/useAuth";

const {username} = useAuth()
export const useUserStore = defineStore('userStore', {
  state: () => ({
    user: null as User | null,
    users: ref<User[]>([]),
    favorites: ref<Place[]>([]),
    loading: false,
    error: null as string | null,
  }),

  getters: {
    isAuthenticated: (state) => !!state.user,
    userId: (state) => state.user?.id || '',
    userName: (state) => state.user?.name || '',
    userRoles: (state) => state.user?.role || [],
    isFavorite: (state) => (placeId: number) => {
      return state.favorites.some((favorite) => favorite.placeId === placeId);
    }
  },

  actions: {
    async fetchUsers() {
      this.loading = true;
      this.error = null;

      try {
        this.users = await userApi.getUsers();
      } catch (error: unknown) {
        if (error instanceof Error) {
          this.error = error.message;
        } else {
          this.error = String(error);
        }
      } finally {
        this.loading = false;
      }
    },

    async SotreConnectedUser() {
      if (this.user) return;
      if (!username) return;
      this.user = await userApi.getCurrentUser()
    },

    async editUser(id: number, user: User) {
      try {
        await userApi.updateUser(user);
        const index = this.users.findIndex(u => u.id === id);
        if (index !== -1) this.users[index] = { ...this.users[index], ...user };
      } catch (error: unknown) {
        if (error instanceof Error) {
          this.error = error.message;
        } else {
          this.error = String(error);
        }
      }
    },

    async removeUser(id: number) {
      try {
        await userApi.deleteUser(id);
        this.users = this.users.filter(user => user.id !== id);
      } catch (error: unknown) {
        if (error instanceof Error) {
          this.error = error.message;
        } else {
          this.error = String(error);
        }
      }
    },

    async fetchFavorites(user_id: number) {
      this.loading = true;
      this.error = null;
      try {
        this.favorites = await userApi.fetchFavorites(user_id);
      } catch (error: unknown) {
        if (error instanceof Error) {
          this.error = error.message;
        } else {
          this.error = String(error);
        }
      } finally {
        this.loading = false
      }
    },

    async addPlaceToFavorites(place_id: number) {
      try {
        if (this.user?.id !== undefined) {
          await userApi.createFavorite(this.user.id, place_id);
        } else {
          throw new Error("User ID is undefined");
        }
        const place = await placeApi.getPlaceById(String(place_id));
        if (place) {
          this.favorites.push(place);
        }
      } catch (error: unknown) {
        if (error instanceof Error) {
          this.error = error.message;
        } else {
          this.error = String(error);
        }
      }
    },

    async removePlaceFromFavorites(place_id: number) {
      if (!this.user) return;
      try {
        await userApi.deleteFavorite(this.user.id, place_id);
        this.favorites = this.favorites.filter(fav => fav.placeId !== place_id);
      }
      catch (error: unknown) {
        if (error instanceof Error) {
          this.error = error.message;
        } else {
          this.error = String(error);
        }
      }
    }
  },
});
