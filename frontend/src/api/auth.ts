// Authentification (register, login, logout)

export interface AuthResponse {
  token: string;
}

interface ApiErrorBody {
  error?: {
    code: string;
    message: string;
  };
}

const errorMessage = (body: ApiErrorBody | null, fallback: string): string =>
  body?.error?.message ?? fallback;

export const registerApi = async (name: string, email: string, password: string): Promise<AuthResponse> => {
  const response = await fetch('/api/v1/auth/register', {
    method: 'POST',
    body: JSON.stringify({ name, email, password }),
    headers: { 'Content-Type': 'application/json' },
  });
  const responseBody = await response.json();

  if (!response.ok) {
    console.error('Registration failed:', response.status, responseBody);
    throw new Error(errorMessage(responseBody, 'Inscription impossible'));
  }

  return responseBody;
}

export const loginApi = async (email: string, password: string): Promise<AuthResponse> => {
  const response = await fetch('/api/v1/auth/login', {
    method: 'POST',
    body: JSON.stringify({ email, password }),
    headers: { 'Content-Type': 'application/json' },
  });
  const text = await response.text();
  let responseBody: ApiErrorBody & Partial<AuthResponse>;
  try {
    responseBody = JSON.parse(text);
  } catch {
    throw new Error('Email ou mot de passe incorrect');
  }

  if (!response.ok) {
    console.error('Login failed:', response.status, responseBody);
    throw new Error(errorMessage(responseBody, 'Email ou mot de passe incorrect'));
  }

  return responseBody as AuthResponse;
}
