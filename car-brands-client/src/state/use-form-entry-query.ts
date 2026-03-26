import { useEffect } from 'react';
import { useQuery, useQueryClient } from '@tanstack/react-query';
import { getFormEntryById } from '../api';
import type { FormEntryDto } from '../types';
import { QUERY_KEYS } from './query-keys';

function addOrReplaceFormEntry(previous: FormEntryDto[] | undefined, nextEntry: FormEntryDto): FormEntryDto[] {
  const current = previous ?? [];
  const withoutUpdated = current.filter((entry) => entry.id !== nextEntry.id);

  return [nextEntry, ...withoutUpdated];
}

export function useFormEntryQuery(id: string | undefined) {
  const queryClient = useQueryClient();

  const query = useQuery({
    queryKey: QUERY_KEYS.formEntryById(id ?? ''),
    queryFn: () => getFormEntryById(id as string),
    enabled: Boolean(id),
    staleTime: 0,
  });

  useEffect(() => {
    if (!query.data) {
      return;
    }

    queryClient.setQueryData<FormEntryDto[]>(
      QUERY_KEYS.sessionFormEntries,
      (previous) => addOrReplaceFormEntry(previous, query.data),
    );
  }, [query.data, queryClient]);

  return query;
}
