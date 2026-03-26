import { useEffect, useMemo, useState } from 'react';
import type { ChangeEvent } from 'react';
import type { FormProps } from '../models';
import type { FormEntryRequestDto, UUID } from '../types';
import {
  STRINGS,
  buildIndentedCarOptions,
  getCarsSelectSize,
  getSelectedMultiValues,
  toFormValues,
} from '../utils';

export function Form({ cars, initialValues, onChange, disabled = false, className }: FormProps) {
  const normalizedInitialValues = useMemo(
    () => toFormValues(initialValues),
    [initialValues],
  );

  const [fullName, setFullName] = useState(normalizedInitialValues.fullName);
  const [contactPhone, setContactPhone] = useState(normalizedInitialValues.contactPhone);
  const [validDrivingLicense, setValidDrivingLicense] = useState(normalizedInitialValues.validDrivingLicense);
  const [selectedCarIds, setSelectedCarIds] = useState<UUID[]>(normalizedInitialValues.selectedCarIds);

  const carOptions = useMemo(() => buildIndentedCarOptions(cars), [cars]);

  const values = useMemo<FormEntryRequestDto>(
    () => ({ fullName, contactPhone, validDrivingLicense, selectedCarIds }),
    [fullName, contactPhone, validDrivingLicense, selectedCarIds],
  );

  useEffect(() => {
    setFullName(normalizedInitialValues.fullName);
    setContactPhone(normalizedInitialValues.contactPhone);
    setValidDrivingLicense(normalizedInitialValues.validDrivingLicense);
    setSelectedCarIds(normalizedInitialValues.selectedCarIds);
  }, [normalizedInitialValues]);

  useEffect(() => {
    onChange?.(values);
  }, [onChange, values]);

  const handleCarSelectionChange = (event: ChangeEvent<HTMLSelectElement>) => {
    setSelectedCarIds(getSelectedMultiValues(event.target.selectedOptions));
  };

  return (
    <form className={`text-start ${className ?? ''}`.trim()} noValidate>
      <div className="mb-3">
        <label htmlFor="fullName" className="form-label">{STRINGS.form.fullName}</label>
        <input
          id="fullName"
          type="text"
          className="form-control"
          value={fullName}
          onChange={(event) => setFullName(event.target.value)}
          disabled={disabled}
        />
      </div>

      <div className="mb-3">
        <label htmlFor="contactPhone" className="form-label">{STRINGS.form.contactPhone}</label>
        <input
          id="contactPhone"
          type="text"
          className="form-control"
          value={contactPhone}
          onChange={(event) => setContactPhone(event.target.value)}
          disabled={disabled}
        />
      </div>

      <div className="mb-3 form-check form-switch">
        <input
          id="validDrivingLicense"
          className="form-check-input"
          type="checkbox"
          checked={validDrivingLicense}
          onChange={(event) => setValidDrivingLicense(event.target.checked)}
          disabled={disabled}
        />
        <label className="form-check-label" htmlFor="validDrivingLicense">
          {STRINGS.form.validDrivingLicense}
        </label>
      </div>

      <div className="mb-3">
        <label htmlFor="carSelect" className="form-label">{STRINGS.form.cars}</label>
        <select
          id="carSelect"
          className="form-select"
          multiple
          size={getCarsSelectSize(carOptions.length)}
          value={selectedCarIds}
          onChange={handleCarSelectionChange}
          disabled={disabled}
        >
          {carOptions.map((car) => (
            <option key={car.id} value={car.id}>
              {car.label}
            </option>
          ))}
        </select>
        <div className="form-text">{STRINGS.form.multiSelectHint}</div>
      </div>
    </form>
  );
}
