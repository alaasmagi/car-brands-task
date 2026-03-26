# car-brands-task

## Short description
* UI language: Estonian
* Development year: **2026**
* Languages and technologies: **Backend: Spring Boot, Java, JPA, SQLite & Frontend: React, TypeScript**

## How to run
### Prerequisites

* Java SDK 25 LTS
* Node.js 
* Modern web browser

Backend should have .env file in the backend root folder `/car-brands-api` which has following content:
```bash
SERVER_PORT=<your-server-port>
FRONTEND_URL=<your-frontend-url>
```
The example has been provided in `/car-brands-api/.env.example`

Frontend should also have .env file in the frontend root folder `/car-brands-client` which has following content:
```bash
VITE_API_BASE_URL=<your-server-url>/api
VITE_FRONTEND_URL=<your-frontend-url>
```
The example has been provided in `/car-brands-client/.env.example`

8080 is the default port on which the backend runs. 5173 is the default port on which the frontend runs.

### Running the app

After meeting all prerequisites above - 
* backend can be run via terminal/cmd open in the `/car-brands-api` folder by executing command:  
```bash
./gradlew bootRun
```
* frontend can be run via terminal/cmd open in the `/car-brands-client` folder by executing command:  
```bash
npm i; npm run dev 
```

## Structure

### Data model

<img width="1320" height="314" alt="image" src="https://github.com/user-attachments/assets/ffb20fab-2afb-4645-a3d7-c8c2f70586a3" />

* **Many-to-Many relationship via 2xOne-to-Many relatioships**- multiple form entries can be linked to multiple cars. The join-table is a result of Java's `@ManyToMany` mapping between the Car and FormEntry entities.

### Backend structure

```
car_brands_api
    ├── Application.java
    ├── application
    │   ├── CarService.java
    │   └── FormEntryService.java
    ├── config
    │   ├── database
    │   │   └── DbInitializer.java
    │   └── web
    │       └── SecurityConfig.java
    ├── contract
    │   ├── application
    │   │   ├── ICarService.java
    │   │   └── IFormEntryService.java
    │   └── data_access
    │       ├── CarJpaRepository.java
    │       ├── ICarRepository.java
    │       ├── IFormEntryJpaRepository.java
    │       ├── IFormEntryRepository.java
    │       └── IRepository.java
    ├── data_access
    │   ├── CarRepository.java
    │   └── FormEntryRepository.java
    ├── domain
    │   ├── Car.java
    │   └── FormEntry.java
    ├── dto
    │   ├── data_access
    │   │   ├── dto
    │   │   │   ├── BaseEntity.java
    │   │   │   ├── CarEntity.java
    │   │   │   └── FormEntryEntity.java
    │   │   └── mapper
    │   │       ├── CarEntityMapper.java
    │   │       └── FormEntryEntityMapper.java
    │   └── web
    │       ├── dto
    │       │   ├── CarDto.java
    │       │   ├── CarRequestDto.java
    │       │   ├── FormEntryDto.java
    │       │   └── FormEntryRequestDto.java
    │       └── mapper
    │           ├── CarDtoMapper.java
    │           └── FormEntryDtoMapper.java
    └── web
        ├── ApiErrorResponse.java
        ├── GlobalExceptionHandler.java
        └── controller
            ├── CarController.java
            └── FormEntryController.java
```

#### Domain layer

* **Car:**

```java
@Getter
@Setter
public class Car {
    private UUID id;
    private String name;
    private UUID parentId;
}
```

* **FormEntry**
```java
@Getter
@Setter
public class FormEntry {
    private UUID id;
    private String fullName;
    private String contactPhone;
    private boolean validDrivingLicense;
    private List<UUID> selectedCarIds;
}
```

#### Application layer
* **CarService** - responsible for validating car data, enforcing business rules, and coordinating CRUD operations via repository contracts.
* **FormEntryService** - responsible for validating form input, resolving selected car relations, and coordinating CRUD operations for form entries.

#### Data access layer
* **CarRepository** - responsible for persistence operations for cars and mapping between domain objects and database entities.
* **FormEntryRepository** - responsible for persistence operations for form entries and mapping of many-to-many relationships with cars.

#### DTOs
DTOs are used to separate external data structures from the core domain model. In the Web layer, request and response DTOs define the API contract and are mapped to domain objects before reaching the service layer. In the DataAccess layer, persistence models are mapped separately so database-specific structures do not leak into the domain or web layers. This keeps the architecture clean, reduces coupling between layers, and makes the application easier to maintain and evolve.


#### Endpoints
* **GET** - `/api/cars`: Fetches all cars.
* **GET** - `/api/cars/{id}`: Fetches a single car by ID.
* **POST** - `/api/cars`: Creates a new car.
* **PUT** - `/api/cars/{id}`: Updates an existing car.
* **DELETE** - `/api/cars/{id}`: Deletes a car.

* **GET** - `/api/form-entries/{id}`: Fetches a single form entry by ID, including resolved selected cars.
* **POST** - `/api/form-entries`: Creates a new form entry.
* **PUT** - `/api/form-entries/{id}`: Updates an existing form entry.
* **DELETE** - `/api/form-entries/{id}`: Deletes a form entry.

### Frontend structure

```
src
  ├── App.css
  ├── index.css
  ├── main.tsx
  ├── api
  │   ├── cars.ts
  │   ├── errors.ts
  │   ├── form-entries.ts
  │   ├── http-client.ts
  │   ├── index.ts
  │   └── request.ts
  ├── components
  │   ├── Form.tsx
  │   └── FormCard.tsx
  ├── models
  │   └── index.ts
  ├── routing
  │   └── router.tsx
  ├── state
  │   ├── index.ts
  │   ├── query-client.ts
  │   ├── query-keys.ts
  │   ├── use-car-options-query.ts
  │   ├── use-form-entry-query.ts
  │   └── use-session-form-entries.ts
  ├── types
  │   ├── api-error.ts
  │   ├── car.ts
  │   ├── form-entry.ts
  │   └── index.ts
  ├── utils
  │   ├── index.ts
  │   ├── strings.ts
  │   └── ui.ts
  └── views
	  ├── Create.tsx
	  ├── Details.tsx
	  ├── Edit.tsx
	  └── Home.tsx
```

* **API** - Axios-based HTTP client modules for backend communication.
* **Routing** - central route declarations for Home/Create/Details/Edit flow.
* **State** - combines TanStack Query for server data operations with a session-scoped client state layer for locally managed entries.
* **Types** - DTO and model typing for safer API and UI integration.
* **Utils** - localized strings and helper utilities for UI logic.

  
