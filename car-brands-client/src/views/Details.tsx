import { Link, useNavigate, useParams } from 'react-router-dom';
import { useDeleteFormEntrySessionMutation, useFormEntryQuery } from '../state';
import { STRINGS } from '../utils';

export function Details() {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const {
    data: entry,
    isLoading,
    isError,
  } = useFormEntryQuery(id);
  const deleteEntryMutation = useDeleteFormEntrySessionMutation();

  const handleDelete = async () => {
    if (!id || deleteEntryMutation.isPending) {
      return;
    }

    const isConfirmed = window.confirm(STRINGS.details.deleteConfirm);
    if (!isConfirmed) {
      return;
    }

    try {
      await deleteEntryMutation.mutateAsync(id);
      navigate('/');
    } catch {
    }
  };

  return (
    <main className="container py-4 text-start">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1 className="h3 mb-0">{STRINGS.details.title}</h1>
        <Link to="/" className="btn btn-outline-secondary">{STRINGS.common.backToHome}</Link>
      </div>

      {!id ? (
        <div className="alert alert-warning" role="alert">
          {STRINGS.details.missingId}
        </div>
      ) : isLoading ? (
        <div className="alert alert-secondary" role="status">
          {STRINGS.details.loading}
        </div>
      ) : isError || !entry ? (
        <div className="alert alert-warning" role="alert">
          {STRINGS.details.notFound}
        </div>
      ) : (
        <div className="card">
          <div className="card-body">
            <p className="mb-2">
              <strong>{STRINGS.form.fullName}:</strong>
              {' '}
              {entry.fullName}
            </p>
            <p className="mb-2">
              <strong>{STRINGS.form.contactPhone}:</strong>
              {' '}
              {entry.contactPhone}
            </p>
            <p className="mb-3">
              <strong>{STRINGS.form.validDrivingLicense}:</strong>
              {' '}
              {entry.validDrivingLicense ? STRINGS.common.yes : STRINGS.common.no}
            </p>

            <h2 className="h5 mb-2">{STRINGS.details.selectedCars}</h2>
            {entry.selectedCars.length === 0 ? (
              <p className="mb-0">{STRINGS.details.noSelectedCars}</p>
            ) : (
              <ul className="mb-0">
                {entry.selectedCars.map((car) => (
                  <li key={car.id}>
                    {car.name}
                  </li>
                ))}
              </ul>
            )}

            {deleteEntryMutation.isError ? (
              <div className="alert alert-danger mt-3 mb-0" role="alert">
                {STRINGS.details.deleteError}
              </div>
            ) : null}
          </div>
          <div className="card-footer d-flex justify-content-end gap-2">
            <Link to={`/edit/${entry.id}`} className="btn btn-outline-primary">
              {STRINGS.details.edit}
            </Link>
            <button
              type="button"
              className="btn btn-danger"
              onClick={handleDelete}
              disabled={deleteEntryMutation.isPending}
            >
              {deleteEntryMutation.isPending ? STRINGS.details.deleting : STRINGS.details.delete}
            </button>
          </div>
        </div>
      )}
    </main>
  );
}
