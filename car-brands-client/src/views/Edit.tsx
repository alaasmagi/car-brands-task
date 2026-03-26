import { useMemo, useState } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Form } from '../components/Form';
import { EMPTY_FORM_ENTRY_VALUES } from '../models';
import type { FormEntryRequestDto } from '../types';
import {
  useCarsQuery,
  useFormEntryQuery,
  useUpdateFormEntrySessionMutation,
} from '../state';
import { STRINGS } from '../utils';

export function Edit() {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const { data: cars = [], isLoading: isCarsLoading, isError: isCarsError } = useCarsQuery();
  const { data: entry, isLoading: isEntryLoading, isError: isEntryError } = useFormEntryQuery(id);
  const updateEntryMutation = useUpdateFormEntrySessionMutation();
  const [values, setValues] = useState<FormEntryRequestDto>(EMPTY_FORM_ENTRY_VALUES);

  const initialValues = useMemo<FormEntryRequestDto | undefined>(() => {
    if (!entry) {
      return undefined;
    }

    return {
      fullName: entry.fullName,
      contactPhone: entry.contactPhone,
      validDrivingLicense: entry.validDrivingLicense,
      selectedCarIds: entry.selectedCars.map((car) => car.id),
    };
  }, [entry]);

  const isSubmitDisabled = useMemo(() => (
    updateEntryMutation.isPending
    || values.fullName.trim().length === 0
    || values.contactPhone.trim().length === 0
    || values.selectedCarIds.length === 0
  ), [updateEntryMutation.isPending, values]);

  const handleSubmit = async () => {
    if (!id || updateEntryMutation.isPending) {
      return;
    }

    try {
      await updateEntryMutation.mutateAsync({ id, payload: values });
      navigate('/');
    } catch {
    }
  };

  return (
    <main className="container py-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1 className="h3 mb-0">{STRINGS.edit.title}</h1>
        <Link to={id ? `/details/${id}` : '/'} className="btn btn-outline-secondary">{STRINGS.edit.back}</Link>
      </div>

      {!id ? (
        <div className="alert alert-warning" role="alert">
          {STRINGS.edit.missingId}
        </div>
      ) : null}

      {id && (isCarsLoading || isEntryLoading) ? (
        <div className="alert alert-secondary" role="status">
          {STRINGS.edit.loadingEntry}
        </div>
      ) : null}

      {id && (isCarsError || isEntryError) ? (
        <div className="alert alert-danger" role="alert">
          {STRINGS.edit.loadEntryError}
        </div>
      ) : null}

      {id && !isCarsLoading && !isEntryLoading && !isCarsError && !isEntryError && !entry ? (
        <div className="alert alert-warning" role="alert">
          {STRINGS.edit.notFound}
        </div>
      ) : null}

      {id && !isCarsLoading && !isEntryLoading && !isCarsError && !isEntryError && entry ? (
        <div className="card">
          <div className="card-body">
            <Form
              cars={cars}
              initialValues={initialValues}
              onChange={setValues}
              disabled={updateEntryMutation.isPending}
            />

            {updateEntryMutation.isError ? (
              <div className="alert alert-danger mt-3 mb-0" role="alert">
                {STRINGS.edit.saveError}
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
              {updateEntryMutation.isPending ? STRINGS.edit.submitting : STRINGS.edit.submit}
            </button>
          </div>
        </div>
      ) : null}
    </main>
  );
}
