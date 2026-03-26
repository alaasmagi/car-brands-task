import axios from 'axios';
import type { ApiErrorResponse } from '../types';
import type { ApiClientError } from '../models';

function isApiErrorResponse(data: unknown): data is ApiErrorResponse {
  if (!data || typeof data !== 'object') {
    return false;
  }

  const candidate = data as Partial<ApiErrorResponse>;

  return (
    typeof candidate.timestamp === 'string'
    && typeof candidate.status === 'number'
    && typeof candidate.error === 'string'
    && typeof candidate.message === 'string'
    && Array.isArray(candidate.details)
  );
}

export function toApiClientError(error: unknown): ApiClientError {
  if (axios.isAxiosError(error)) {
    const responseData = error.response?.data;

    if (isApiErrorResponse(responseData)) {
      return {
        status: responseData.status,
        message: responseData.message,
        details: responseData.details,
        timestamp: responseData.timestamp,
        raw: error,
      };
    }

    return {
      status: error.response?.status ?? null,
      message: error.message || 'Unexpected API error',
      details: [],
      timestamp: null,
      raw: error,
    };
  }

  if (error instanceof Error) {
    return {
      status: null,
      message: error.message,
      details: [],
      timestamp: null,
      raw: error,
    };
  }

  return {
    status: null,
    message: 'Unknown error',
    details: [],
    timestamp: null,
    raw: error,
  };
}
