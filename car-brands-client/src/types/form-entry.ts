import type { CarDto, UUID } from './car';

export interface FormEntryRequestDto {
  fullName: string;
  contactPhone: string;
  validDrivingLicense: boolean;
  selectedCarIds: UUID[];
}

export interface FormEntryDto {
  id: UUID;
  fullName: string;
  contactPhone: string;
  validDrivingLicense: boolean;
  selectedCars: CarDto[];
}
