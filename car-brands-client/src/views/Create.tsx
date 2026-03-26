import { useMemo, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Form } from '../components/Form';
import { EMPTY_FORM_ENTRY_VALUES } from '../models';
import type { FormEntryRequestDto } from '../types';
import { useCarsQuery, useCreateFormEntrySessionMutation } from '../state';
import { STRINGS } from '../utils';

export function Create() {
  const navigate = useNavigate();
  const { data: cars = [], isLoading, isError } = useCarsQuery();
  const createEntryMutation = useCreateFormEntrySessionMutation();
  const [values, setValues] = useState<FormEntryRequestDto>(EMPTY_FORM_ENTRY_VALUES);

  const isSubmitDisabled = useMemo(() => (
    createEntryMutation.isPending
    || values.fullName.trim().length === 0
    || values.contactPhone.trim().length === 0
    || values.selectedCarIds.length === 0
  ), [createEntryMutation.isPending, values]);

  const handleSubmit = async () => {
    try {
      await createEntryMutation.mutateAsync(values);
      navigate('/');
    } catch {
    }
  };

  return (
    <main className="container py-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1 className="h3 mb-0">{STRINGS.create.title}</h1>
        <Link to="/" className="btn btn-outline-secondary">{STRINGS.common.backToHome}</Link>
      </div>

      {isLoading ? (
        <div className="alert alert-secondary" role="status">{STRINGS.create.loadingCars}</div>
      ) : null}

      {isError ? (
        <div className="alert alert-danger" role="alert">
          {STRINGS.create.loadCarsError}
        </div>
      ) : null}

      {!isLoading && !isError ? (
        <div className="card">
          <div className="card-body">
            <Form
              cars={cars}
              onChange={setValues}
              disabled={createEntryMutation.isPending}
            />

            {createEntryMutation.isError ? (
              <div className="alert alert-danger mt-3 mb-0" role="alert">
                {STRINGS.create.saveError}
              </div>
            ) : null}
          </div>
          <div className="card-footer d-flex justify-content-end">
            <button
              type="button"
              className="btn btn-primary"
              onClick={handleSubmit}
              disabled={isSubmitDisabled}
            >
              {createEntryMutation.isPending ? STRINGS.create.submitting : STRINGS.create.submit}
            </button>
          </div>
        </div>
      ) : null}
    </main>
  );
}
