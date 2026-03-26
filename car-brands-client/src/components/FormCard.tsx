import { Link } from 'react-router-dom';
import type { FormCardProps } from '../models';
import { STRINGS } from '../utils';

export function FormCard({ entry, orderNumber }: FormCardProps) {
  return (
    <article className="card text-start h-100">
      <div className="card-body">
        <div className="d-flex justify-content-between align-items-start gap-3">
          <div>
            <div className="d-flex align-items-center gap-2 mb-2">
              <h2 className="h5 card-title mb-0">
                <span className="h5 text-body-secondary me-2">#{orderNumber}</span>
                {entry.fullName}
              </h2>
            </div>
            <p className="card-text mb-1">
              <strong>{STRINGS.form.contactPhone}:</strong>
              {' '}
              {entry.contactPhone}
            </p>
            <p className="card-text mb-0">
              <strong>{STRINGS.form.validDrivingLicense}:</strong>
              {' '}
              {entry.validDrivingLicense ? STRINGS.common.yes : STRINGS.common.no}
            </p>
          </div>

          <Link to={`/details/${entry.id}`} className="btn btn-outline-primary btn-sm">
            {STRINGS.formCard.showDetails}
          </Link>
        </div>
      </div>
    </article>
  );
}
