<template>
  <!-- Contenu principal -->
  <main class="flex-1 flex items-center justify-center p-4">
    <div class="w-full max-w-md">
      <!-- Onglets de connexion/inscription -->
      <div class="bg-white rounded-t-lg shadow-md overflow-hidden">
        <div class="flex">
          <button @click="activeTab = 'login'; if (route.path.includes('/register')) router.push('/login')"
            class="flex-1 py-4 text-center font-medium transition-colors"
            :class="activeTab === 'login' ? 'bg-emerald-600 text-white' : 'bg-gray-100 text-gray-700 hover:bg-gray-200'">
            Connexion
          </button>
          <button @click="activeTab = 'register'; if (route.path.includes('/login')) router.push('/register')"
            class="flex-1 py-4 text-center font-medium transition-colors"
            :class="activeTab === 'register' ? 'bg-emerald-600 text-white' : 'bg-gray-100 text-gray-700 hover:bg-gray-200'">
            Inscription
          </button>
        </div>
      </div>

      <!-- Formulaire de connexion -->
      <div v-if="activeTab === 'login'" class="bg-white rounded-b-lg shadow-md p-6">
        <h2 class="text-2xl font-bold text-gray-800 mb-6">Connexion</h2>

        <form @submit.prevent="handleLogin" class="space-y-4">
          <div>
            <label for="email" class="block text-sm font-medium text-gray-700 mb-1">Email</label>
            <input type="email" id="email" v-model="loginForm.email" placeholder="votre@email.com" required
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:border-transparent" />
          </div>

          <div>
            <label for="password" class="block text-sm font-medium text-gray-700 mb-1">Mot de passe</label>
            <input type="password" id="password" v-model="loginForm.password" placeholder="••••••••" required
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:border-transparent" />
          </div>

          <div class="flex items-center justify-between">
            <div class="flex items-center">
              <input type="checkbox" id="remember" v-model="loginForm.remember"
                class="h-4 w-4 text-emerald-600 focus:ring-emerald-500 border-gray-300 rounded" />
              <label for="remember" class="ml-2 block text-sm text-gray-700">
                Se souvenir de moi
              </label>
            </div>
            <a href="#" class="text-sm text-emerald-600 hover:underline">
              Mot de passe oublié ?
            </a>
          </div>

          <div>
            <button type="submit"
              class="w-full px-4 py-2 bg-emerald-600 text-white rounded-md hover:bg-emerald-700 transition-colors"
              :disabled="isLoading">
              <span v-if="isLoading">Connexion en cours...</span>
              <span v-else>Se connecter</span>
            </button>
            <p v-if="errorMessage" class="mt-2 text-sm text-red-600">{{ errorMessage }}</p>
          </div>
        </form>

        <div class="mt-6">
          <div class="relative">
            <div class="absolute inset-0 flex items-center">
              <div class="w-full border-t border-gray-300"></div>
            </div>
            <div class="relative flex justify-center text-sm">
              <span class="px-2 bg-white text-gray-500">Ou continuer avec</span>
            </div>
          </div>

          <div class="mt-6 grid grid-cols-2 gap-3">
            <button type="button"
              class="w-full inline-flex justify-center py-2 px-4 border border-gray-300 rounded-md shadow-sm bg-white text-sm font-medium text-gray-700 hover:bg-gray-50">
              <FacebookIcon class="h-5 w-5 text-blue-600" />
              <span class="ml-2">Facebook</span>
            </button>
            <button type="button"
              class="w-full inline-flex justify-center py-2 px-4 border border-gray-300 rounded-md shadow-sm bg-white text-sm font-medium text-gray-700 hover:bg-gray-50">
              <GoogleIcon class="h-5 w-5 text-red-600" />
              <span class="ml-2">Google</span>
            </button>
          </div>
        </div>
      </div>

      <!-- Formulaire d'inscription -->
      <div v-if="activeTab === 'register'" class="bg-white rounded-b-lg shadow-md p-6">
        <h2 class="text-2xl font-bold text-gray-800 mb-6">Inscription</h2>

        <form @submit.prevent="handleRegister" class="space-y-4">
          <div class="grid grid-cols-2 gap-4">
            <div>
              <label for="firstName" class="block text-sm font-medium text-gray-700 mb-1">Prénom</label>
              <input type="text" id="firstName" v-model="registerForm.firstName" placeholder="Jean" required
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:border-transparent" />
            </div>
            <div>
              <label for="lastName" class="block text-sm font-medium text-gray-700 mb-1">Nom</label>
              <input type="text" id="lastName" v-model="registerForm.lastName" placeholder="Dupont" required
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:border-transparent" />
            </div>
          </div>

          <div>
            <label for="registerEmail" class="block text-sm font-medium text-gray-700 mb-1">Email</label>
            <input type="email" id="registerEmail" v-model="registerForm.email" placeholder="votre@email.com" required
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:border-transparent" />
          </div>

            <div>
            <label for="registerPassword" class="block text-sm font-medium text-gray-700 mb-1">Mot de passe</label>
            <div class="relative">
              <input :type="showPassword ? 'text' : 'password'" id="registerPassword" v-model="registerForm.password" placeholder="••••••••" required
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:border-transparent" />
              <button type="button" @click="showPassword = !showPassword" class="absolute inset-y-0 right-0 px-3 py-2 text-gray-600">
              <span v-if="showPassword">Hide</span>
              <span v-else>Show</span>
              </button>
            </div>
            <p class="mt-1 text-xs text-gray-500">Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule et un chiffre.</p>
            </div>

            <div>
            <label for="confirmPassword" class="block text-sm font-medium text-gray-700 mb-1">Confirmer le mot de passe</label>
            <div class="relative">
              <input :type="showConfirmPassword ? 'text' : 'password'" id="confirmPassword" v-model="registerForm.confirmPassword" placeholder="••••••••" required
              class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-emerald-500 focus:border-transparent" />
              <button type="button" @click="showConfirmPassword = !showConfirmPassword" class="absolute inset-y-0 right-0 px-3 py-2 text-gray-600">
              <span v-if="showConfirmPassword">Hide</span>
              <span v-else>Show</span>
              </button>
            </div>
            </div>

          <div class="flex items-center">
            <input type="checkbox" id="terms" v-model="registerForm.terms" required
              class="h-4 w-4 text-emerald-600 focus:ring-emerald-500 border-gray-300 rounded" />
            <label for="terms" class="ml-2 block text-sm text-gray-700">
              J'accepte les <a href="#" class="text-emerald-600 hover:underline">conditions d'utilisation</a> et la <a
                href="#" class="text-emerald-600 hover:underline">politique de confidentialité</a>
            </label>
          </div>

          <div>
            <button type="submit"
              class="w-full px-4 py-2 bg-emerald-600 text-white rounded-md hover:bg-emerald-700 transition-colors"
              :disabled="isLoading">
              <span v-if="isLoading">Inscription en cours...</span>
              <span v-else>S'inscrire</span>
            </button>
            <p v-if="errorMessage" class="mt-2 text-sm text-red-600">{{ errorMessage }}</p>
          </div>
        </form>

        <div class="mt-6">
          <div class="relative">
            <div class="absolute inset-0 flex items-center">
              <div class="w-full border-t border-gray-300"></div>
            </div>
            <div class="relative flex justify-center text-sm">
              <span class="px-2 bg-white text-gray-500">Ou s'inscrire avec</span>
            </div>
          </div>

          <div class="mt-6 grid grid-cols-2 gap-3">
            <button type="button"
              class="w-full inline-flex justify-center py-2 px-4 border border-gray-300 rounded-md shadow-sm bg-white text-sm font-medium text-gray-700 hover:bg-gray-50">
              <FacebookIcon class="h-5 w-5 text-blue-600" />
              <span class="ml-2">Facebook</span>
            </button>
            <button type="button"
              class="w-full inline-flex justify-center py-2 px-4 border border-gray-300 rounded-md shadow-sm bg-white text-sm font-medium text-gray-700 hover:bg-gray-50">
              <GoogleIcon class="h-5 w-5 text-red-600" />
              <span class="ml-2">Google</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>

<script lang="ts" setup>
import { reactive, ref } from 'vue';
import { useAuth } from '@/composables/useAuth';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/useUserStore';

const { login, register } = useAuth();
const router = useRouter();

// État des onglets
const activeTab = ref('login');
const route = useRouter().currentRoute;

if (route.value.path.includes('/register')) {
  activeTab.value = 'register';
} else if (route.value.path.includes('/login')) {
  activeTab.value = 'login';
}

// État de chargement
const isLoading = ref(false);

// Formulaire de connexion
const loginForm = reactive({
  email: '',
  password: '',
  remember: false
});

// Formulaire d'inscription
const registerForm = reactive({
  firstName: '',
  lastName: '',
  email: '',
  password: '',
  confirmPassword: '',
  terms: false
});


const userStore = useUserStore();
const errorMessage = ref('');
const showPassword = ref(false);
const showConfirmPassword = ref(false);
const handleLogin = async () => {
  console.log('Tentative de connexion avec :', loginForm);
  try {
    await login(loginForm.email, loginForm.password);
    await userStore.SotreConnectedUser();
    const redirectTo = Array.isArray(route.value.query.redirect) ? route.value.query.redirect[0] : route.value.query.redirect;
    router.push(typeof redirectTo === 'string' ? redirectTo : '/'); // Redirection vers la page d'accueil après connexion
  } catch (error) {
    errorMessage.value = 'Identifiants incorrects';
    console.error('Erreur de connexion :', error);

  }
};
const handleRegister = async () => {
  console.log('Tentative d\'inscription avec :', registerForm);
  const fullname = [registerForm.firstName, registerForm.lastName].filter(Boolean).join(' ');
  // Validation du mot de passe
  if (registerForm.password !== registerForm.confirmPassword) {
    errorMessage.value = 'Les mots de passe ne correspondent pas';
    return;
  }

  // Ajouter ici la logique d'inscription avec API
  try {
    await register(fullname, registerForm.email, registerForm.password);
    errorMessage.value = 'Inscription réussie';
    router.push('/login'); // Redirection vers la page d'accueil après inscription
  } catch (error) {
    errorMessage.value = 'Erreur lors de l\'inscription';
    console.error('Erreur d\'inscription :', error);
  }
};

</script>

<style scoped></style>
