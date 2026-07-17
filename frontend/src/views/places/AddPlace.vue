<template>
  <div class="container mx-auto px-4 py-8">
    <div class="max-w-3xl mx-auto">
      <h1 class="text-3xl font-bold mb-8">Ajouter un nouveau lieu</h1>

      <div class="bg-white rounded-lg shadow-md p-6">
        <form @submit.prevent="submitPlace">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mb-6">
            <div class="col-span-1 md:col-span-2">
              <label for="name" class="block text-sm font-medium text-gray-700 mb-1">Nom du lieu *</label>
              <input
                type="text"
                id="name"
                v-model="placeData.name"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"
                required
              />
            </div>

            <div class="col-span-1 md:col-span-2">
              <label for="description" class="block text-sm font-medium text-gray-700 mb-1">Description *</label>
              <textarea
                id="description"
                v-model="placeData.description"
                rows="4"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"
                required
              ></textarea>
            </div>

            <div>
              <label for="category" class="block text-sm font-medium text-gray-700 mb-1">Catégorie *</label>
              <select
                id="category"
                v-model="placeData.categoryId"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"
                required
              >
                <option value="" disabled>Sélectionnez une catégorie</option>
                <option
                  v-for="category in categories"
                  :key="category.categoryId"
                  :value="category.categoryId"
                >
                  {{ category.name }}
                </option>
              </select>
            </div>

            <div>
              <label for="image" class="block text-sm font-medium text-gray-700 mb-1">URL de l'image *</label>
              <input
                type="url"
                id="image"
                v-model="placeData.website"
                placeholder="https://example.com/image.jpg"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"
                required
              />
            </div>

            <div>
              <label for="address" class="block text-sm font-medium text-gray-700 mb-1">Adresse *</label>
              <input
                type="text"
                id="address"
                v-model="placeData.address"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"
                required
              />
            </div>

            <div>
              <label for="city" class="block text-sm font-medium text-gray-700 mb-1">Ville *</label>
              <input
                type="text"
                id="city"
                v-model="placeData.city"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"
                required
              />
            </div>

            <div>
              <label for="postal" class="block text-sm font-medium text-gray-700 mb-1">Code postal *</label>
              <input
                type="text"
                id="postal"
                v-model="placeData.zip"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-primary focus:border-transparent"
                required
              />
            </div>
          </div>

          <div class="flex justify-end gap-4">
            <router-link
              to="/lieux"
              class="px-6 py-2 border border-gray-300 rounded-md hover:bg-gray-50 transition-colors"
            >
              Annuler
            </router-link>
            <button
              type="submit"
              class="px-6 py-2 bg-primary text-white rounded-md hover:bg-primary-dark transition-colors"
              :disabled="submitting"
            >
              <span v-if="submitting">Envoi en cours...</span>
              <span v-else>Ajouter le lieu</span>
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { usePlaceStore } from '@/stores/usePlaceStore';
import { useUserStore } from '@/stores/useUserStore';
import { useCategoryStore } from '@/stores/useCategoryStore';
import type { Category } from '@/api/categoryApi';
import type { CreatePlacePayload } from '@/api/placeApi';

const router = useRouter();
const placeStore = usePlaceStore();
const userStore = useUserStore();
const categoryStore = useCategoryStore();

const categories = ref<Category[]>([]);
const submitting = ref(false);
const placeData = ref({
  name: '',
  description: '',
  address: '',
  city: '',
  zip: '',
  country: null as string | null,
  website: null as string | null,
  phone: null as string | null,
  categoryId: 0,
});

const isAuthenticated = computed(() => userStore.isAuthenticated);

onMounted(async () => {
  if (!isAuthenticated.value) {
    router.push('/login');
    return;
  }

  try {
    await categoryStore.fetchCategories();
    categories.value = categoryStore.categories;
  } catch (err) {
    console.error("Erreur lors du chargement des catégories:", err);
  }
});

async function submitPlace() {
  if (!isAuthenticated.value) {
    router.push('/login');
    return;
  }

  submitting.value = true;
  try {
    const payload: CreatePlacePayload = {
      ...placeData.value,
      categoryId: Number(placeData.value.categoryId),
    };

    const newPlace = await placeStore.addPlace(payload);

    if (newPlace?.placeId) {
      router.push(`/lieux/${newPlace.placeId}`);
    }
  } catch (err) {
    console.error("Erreur lors de la création du lieu:", err);
  } finally {
    submitting.value = false;
  }
}
</script>

<style scoped>
.bg-primary {
  background-color: #4f46e5;
}
.bg-primary-dark {
  background-color: #4338ca;
}
.text-primary {
  color: #4f46e5;
}
</style>
