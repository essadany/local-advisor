import { loginApi, registerApi } from '@/api/auth'
import { ref } from 'vue'
import { jwtDecode } from 'jwt-decode'

const token = ref(localStorage.getItem('token') || '')
const username = ref('')

const decodeToken = (t: string) => {
  const decoded = jwtDecode<{ sub: string }>(t)
  return decoded.sub
}

if (token.value) {
  username.value = decodeToken(token.value)
}

export function useAuth() {
  const register = async (name: string, email: string, password: string) => {
    try {
      const data = await registerApi(name, email, password)
      token.value = data.token
      return data
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  const login = async (email: string, password: string) => {
    try {
      const data = await loginApi(email, password)
      token.value = data.token
      localStorage.setItem('token', data.token)
      username.value = decodeToken(data.token)
    } catch (error) {
      console.error(error)
      throw error
    }
  }

  const logout = () => {
    token.value = ''
    username.value = ''
    localStorage.removeItem('token')
  }

  return { token, register, login, logout, username }
}
