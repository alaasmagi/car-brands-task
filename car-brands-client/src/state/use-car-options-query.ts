import { useQuery } from '@tanstack/react-query';
import { getCars } from '../api';
import type { CarDto } from '../types';
import { buildIndentedCarOptions } from '../utils';
import { QUERY_KEYS } from './query-keys';

function toCarsArray(value: unknown): CarDto[] {
  return Array.isArray(value) ? value as CarDto[] : [];
}

export function useCarsQuery() {
  return useQuery<CarDto[]>({
    queryKey: QUERY_KEYS.cars,
    queryFn: getCars,
    select: toCarsArray,
  });
}

export function useCarOptionsQuery() {
  return useQuery({
    queryKey: QUERY_KEYS.cars,
    queryFn: getCars,
    select: (response) => buildIndentedCarOptions(toCarsArray(response)),
    staleTime: 10 * 60 * 1000,
  });
}
