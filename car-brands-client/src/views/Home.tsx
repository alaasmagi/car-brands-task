import { Link } from 'react-router-dom';
import { FormCard } from '../components/FormCard';
import { useSessionFormEntriesState } from '../state';
import { STRINGS } from '../utils';

export function Home() {
  const { entries } = useSessionFormEntriesState();

  return (
    <main className="container py-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1 className="h3 mb-0">{STRINGS.home.title}</h1>
        <Link to="/create" className="btn btn-primary">
          {STRINGS.home.addForm}
        </Link>
      </div>

      {entries.length === 0 ? (
        <div className="alert alert-secondary mb-0" role="status">
          {STRINGS.home.emptyState}
        </div>
      ) : (
        <div className="row g-3">
          {entries.map((entry, index) => (
            <div key={entry.id} className="col-12">
              <FormCard entry={entry} orderNumber={index + 1} />
            </div>
          ))}
        </div>
      )}
    </main>
  );
}
