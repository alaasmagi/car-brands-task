import axios from 'axios';

const DEFAULT_API_BASE_URL = 'http://localhost:8080/api';

export const httpClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? DEFAULT_API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});
