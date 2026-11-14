// Authentification (register, login, logout)

export interface AuthResponse {
  token: string;
}

export const registerApi = async (name: string, email:string, password: string) : Promise<AuthResponse> => {
  const response = await fetch('/api/v1/auth/register', {
    method: 'POST',
    body: JSON.stringify({ name, email, password, role: 'USER' }),
    headers: { 'Content-Type': 'application/json' },
  });
  const responseBody = await response.json();

  if (!response.ok) {
    console.error('Registration failed:', response.status, responseBody);
    throw new Error(responseBody.message);
  }

  return responseBody;
}

export const loginApi = async (email: string, password: string) : Promise<AuthResponse> => {
  const response = await fetch('/api/v1/auth/login', {
    method: 'POST',
    body: JSON.stringify({ email, password }),
    headers: { 'Content-Type': 'application/json' },
  });
  const text = await response.text();
  let responseBody: any;
  try {
    responseBody = JSON.parse(text);
  } catch {
    throw new Error('Email ou mot de passe incorrect');
  }

  if (!response.ok) {
    console.error('Login failed:', response.status, responseBody);
    throw new Error(responseBody.message || 'Email ou mot de passe incorrect');
  }

  return responseBody;
}

