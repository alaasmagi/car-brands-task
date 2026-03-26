import type { CarDto, FormEntryDto, FormEntryRequestDto, UUID } from '../types';

export const EMPTY_FORM_ENTRY_VALUES: FormEntryRequestDto = {
  fullName: '',
  contactPhone: '',
  validDrivingLicense: false,
  selectedCarIds: [],
};

export interface CarSelectOption {
  id: UUID;
  name: string;
  depth: number;
  label: string;
}

export interface FormProps {
  cars: CarDto[];
  initialValues?: Partial<FormEntryRequestDto>;
  onChange?: (values: FormEntryRequestDto) => void;
  disabled?: boolean;
  className?: string;
}

export interface FormCardProps {
  entry: FormEntryDto;
  orderNumber: number;
}

export interface UpdateFormEntryPayload {
  id: string;
  payload: FormEntryRequestDto;
}
