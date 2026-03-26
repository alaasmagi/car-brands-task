import type { CarSelectOption } from '../models';
import { EMPTY_FORM_ENTRY_VALUES } from '../models';
import type { CarDto, FormEntryRequestDto, UUID } from '../types';
import { STRINGS } from './strings';

function byName(a: CarDto, b: CarDto): number {
  const aName = typeof a.name === 'string' ? a.name : '';
  const bName = typeof b.name === 'string' ? b.name : '';

  return aName.localeCompare(bName, undefined, { sensitivity: 'base' });
}

function isValidCarDto(car: CarDto): boolean {
  return typeof car.id === 'string' && car.id.length > 0;
}

export function getUniqueIds(ids: UUID[]): UUID[] {
  return Array.from(new Set(ids));
}

export function toFormValues(initialValues?: Partial<FormEntryRequestDto>): FormEntryRequestDto {
  return {
    fullName: initialValues?.fullName ?? EMPTY_FORM_ENTRY_VALUES.fullName,
    contactPhone: initialValues?.contactPhone ?? EMPTY_FORM_ENTRY_VALUES.contactPhone,
    validDrivingLicense: initialValues?.validDrivingLicense ?? EMPTY_FORM_ENTRY_VALUES.validDrivingLicense,
    selectedCarIds: getUniqueIds(initialValues?.selectedCarIds ?? EMPTY_FORM_ENTRY_VALUES.selectedCarIds),
  };
}

export function getSelectedMultiValues(selectedOptions: HTMLCollectionOf<HTMLOptionElement>): UUID[] {
  return getUniqueIds(Array.from(selectedOptions, (option) => option.value));
}

export function getCarsSelectSize(optionsCount: number): number {
  return Math.min(Math.max(optionsCount, 4), 10);
}

export function buildIndentedCarOptions(cars: CarDto[]): CarSelectOption[] {
  if (!Array.isArray(cars)) {
    return [];
  }

  const byParentId = new Map<UUID | null, CarDto[]>();
  const byId = new Map<UUID, CarDto>();
  const normalizedCars = cars.filter(isValidCarDto);

  for (const car of normalizedCars) {
    byId.set(car.id, car);
  }

  for (const car of normalizedCars) {
    const resolvedParentId = car.parentId && byId.has(car.parentId)
      ? car.parentId
      : null;

    const siblings = byParentId.get(resolvedParentId);

    if (siblings) {
      siblings.push(car);
    } else {
      byParentId.set(resolvedParentId, [car]);
    }
  }

  for (const siblings of byParentId.values()) {
    siblings.sort(byName);
  }

  const options: CarSelectOption[] = [];
  const visited = new Set<UUID>();

  const walk = (parentId: UUID | null, depth: number) => {
    const children = byParentId.get(parentId) ?? [];

    for (const child of children) {
      if (visited.has(child.id)) {
        continue;
      }

      visited.add(child.id);
      const indent = depth > 0 ? '\u00A0\u00A0\u00A0\u00A0'.repeat(depth) : '';

      options.push({
        id: child.id,
        name: typeof child.name === 'string' ? child.name : '',
        depth,
        label: `${indent}${typeof child.name === 'string' ? child.name : STRINGS.form.unnamedCar}`,
      });

      walk(child.id, depth + 1);
    }
  };

  walk(null, 0);

  return options;
}
