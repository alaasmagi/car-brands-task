export const QUERY_KEYS = {
  cars: ['cars'] as const,
  carOptions: ['cars', 'options'] as const,
  sessionFormEntries: ['session', 'form-entries'] as const,
  formEntryById: (id: string) => ['form-entries', id] as const,
};
