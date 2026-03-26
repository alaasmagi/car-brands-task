import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { createFormEntry, deleteFormEntry, updateFormEntry } from '../api';
import type { UpdateFormEntryPayload } from '../models';
import type { FormEntryDto, FormEntryRequestDto } from '../types';
import { QUERY_KEYS } from './query-keys';

const EMPTY_FORM_ENTRIES: FormEntryDto[] = [];

function addOrReplaceFormEntry(previous: FormEntryDto[] | undefined, createdEntry: FormEntryDto): FormEntryDto[] {
  const current = previous ?? EMPTY_FORM_ENTRIES;
  const withoutCreated = current.filter((entry) => entry.id !== createdEntry.id);

  return [createdEntry, ...withoutCreated];
}

export function useSessionFormEntriesState() {
  const queryClient = useQueryClient();

  const query = useQuery({
    queryKey: QUERY_KEYS.sessionFormEntries,
    queryFn: () => Promise.resolve(EMPTY_FORM_ENTRIES),
    initialData: EMPTY_FORM_ENTRIES,
    staleTime: Infinity,
    gcTime: Infinity,
  });

  const setEntries = (entries: FormEntryDto[]) => {
    queryClient.setQueryData<FormEntryDto[]>(QUERY_KEYS.sessionFormEntries, entries);
  };

  const addEntry = (entry: FormEntryDto) => {
    queryClient.setQueryData<FormEntryDto[]>(
      QUERY_KEYS.sessionFormEntries,
      (previous) => addOrReplaceFormEntry(previous, entry),
    );
  };

  const removeEntry = (entryId: string) => {
    queryClient.setQueryData<FormEntryDto[]>(
      QUERY_KEYS.sessionFormEntries,
      (previous = EMPTY_FORM_ENTRIES) => previous.filter((entry) => entry.id !== entryId),
    );
  };

  const clearEntries = () => {
    queryClient.setQueryData<FormEntryDto[]>(QUERY_KEYS.sessionFormEntries, EMPTY_FORM_ENTRIES);
  };

  return {
    ...query,
    entries: query.data ?? EMPTY_FORM_ENTRIES,
    setEntries,
    addEntry,
    removeEntry,
    clearEntries,
  };
}

export function useCreateFormEntrySessionMutation() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (payload: FormEntryRequestDto) => createFormEntry(payload),
    onSuccess: (createdEntry) => {
      queryClient.setQueryData<FormEntryDto[]>(
        QUERY_KEYS.sessionFormEntries,
        (previous) => addOrReplaceFormEntry(previous, createdEntry),
      );
    },
  });
}

export function useDeleteFormEntrySessionMutation() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (id: string) => deleteFormEntry(id),
    onSuccess: (_, deletedId) => {
      queryClient.setQueryData<FormEntryDto[]>(
        QUERY_KEYS.sessionFormEntries,
        (previous = EMPTY_FORM_ENTRIES) => previous.filter((entry) => entry.id !== deletedId),
      );
      queryClient.removeQueries({ queryKey: QUERY_KEYS.formEntryById(deletedId) });
    },
  });
}

export function useUpdateFormEntrySessionMutation() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ id, payload }: UpdateFormEntryPayload) => updateFormEntry(id, payload),
    onSuccess: (updatedEntry) => {
      queryClient.setQueryData<FormEntryDto[]>(
        QUERY_KEYS.sessionFormEntries,
        (previous) => addOrReplaceFormEntry(previous, updatedEntry),
      );
      queryClient.setQueryData<FormEntryDto>(QUERY_KEYS.formEntryById(updatedEntry.id), updatedEntry);
    },
  });
}
