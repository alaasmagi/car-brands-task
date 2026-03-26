import type { AxiosResponse } from 'axios';
import { toApiClientError } from './errors';

export async function apiRequest<T>(request: () => Promise<AxiosResponse<T>>): Promise<T> {
  try {
    const response = await request();
    return response.data;
  } catch (error: unknown) {
    throw toApiClientError(error);
  }
}

export async function apiRequestVoid(request: () => Promise<AxiosResponse>): Promise<void> {
  try {
    await request();
  } catch (error: unknown) {
    throw toApiClientError(error);
  }
}
