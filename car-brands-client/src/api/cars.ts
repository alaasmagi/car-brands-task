import { httpClient } from './http-client';
import { apiRequest } from './request';
import type { CarDto, UUID } from '../types';

export function getCars(): Promise<CarDto[]> {
  return apiRequest(() => httpClient.get<CarDto[]>('/cars'));
}

export function getCarById(id: UUID): Promise<CarDto> {
  return apiRequest(() => httpClient.get<CarDto>(`/cars/${id}`));
}
