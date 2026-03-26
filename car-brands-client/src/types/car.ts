export type UUID = string;

export interface CarRequestDto {
  name: string;
  parentId?: UUID | null;
}

export interface CarDto {
  id: UUID;
  name: string;
  parentId: UUID | null;
}
