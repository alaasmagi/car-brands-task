import { httpClient } from './http-client';
import { apiRequest, apiRequestVoid } from './request';
import type { FormEntryDto, FormEntryRequestDto, UUID } from '../types';

export function getFormEntryById(id: UUID): Promise<FormEntryDto> {
  return apiRequest(() => httpClient.get<FormEntryDto>(`/form-entries/${id}`));
}

export function createFormEntry(payload: FormEntryRequestDto): Promise<FormEntryDto> {
  return apiRequest(() => httpClient.post<FormEntryDto>('/form-entries', payload));
}

export function updateFormEntry(id: UUID, payload: FormEntryRequestDto): Promise<FormEntryDto> {
  return apiRequest(() => httpClient.put<FormEntryDto>(`/form-entries/${id}`, payload));
}

export function deleteFormEntry(id: UUID): Promise<void> {
  return apiRequestVoid(() => httpClient.delete(`/form-entries/${id}`));
}
