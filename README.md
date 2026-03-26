# car-brands-task

## Short description
* UI language: Estonian
* Development year: **2026**
* Languages and technologies: **Backend: Spring Boot, Java, JPA, SQLite & Frontend: React, TypeScript**

## How to run - the easy way
### Prerequisites

* Docker
* Modern web browser

The project includes a root-level `docker-compose.yml` file which starts both the backend and the frontend together.

Optional environment variables can be provided in your shell or in a root `.env` file before starting Docker:
```bash
API_PORT=8080
API_INTERNAL_PORT=8080
FRONTEND_PORT=5173
FRONTEND_URL=http://localhost:5173
VITE_API_BASE_URL=http://localhost:8080/api
VITE_FRONTEND_URL=http://localhost:5173
```
If you do not provide them, Docker Compose uses the default values shown above.

### Running the app

To build and start the full application stack, run from the project root:
```bash
docker compose up --build
```

After startup:
* frontend is available at `http://localhost:5173`
* backend is available at `http://localhost:8080`

The SQLite database is stored in a Docker volume called `api-data`, so backend data persists between container restarts.

## How to run - the normal way
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
* Database dump file `database_dump.sql` is in the root of the project.

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

#### Components
* **Form** - reusable form component for collecting full name, contact phone, driving license status, and selected car IDs.
* **FormCard** - visual summary card for a form entry shown on the home list.

#### Views
* **Home** - displays session entries and allows navigation to create new form entries.
* **Create** - allows users to create a new form entry.
* **Details** - displays one form entry with selected car details and supports deletion.
* **Edit** - allows updating an existing form entry.

## Design choices

### Database
SQLite was chosen to keep setup simple and lightweight for local development and demonstration. UUID identifiers are used to reduce collision risk and keep IDs globally unique. The form-entry-to-car relation allows each form entry to reference multiple selected cars while keeping the domain model simple `selectedCarIds` and delegating relational mapping details to the data-access layer.

### Backend
The backend follows a layered architecture with clear separation of concerns: domain models contain core business data, application services hold business logic, repositories isolate persistence concerns, and web controllers expose HTTP endpoints. Additional DTO mapping layers are used to decouple API and persistence shapes from domain objects. This structure improves maintainability and makes refactoring safer.

### Frontend
The frontend is implemented as a focused React SPA. It uses React Router for a clear CRUD navigation flow and TanStack Query for asynchronous data fetching and mutation state management. The UI is component-driven and keeps form logic reusable across create and edit screens. Estonian localization strings are centralized to maintain consistency. Customisation is done using Bootstrap and, therefore, the UI is responsive (mobile phone friendly).

## Features

Main application features include:

* Car brands/models listing via API
* Form entry creation with validation
* Form entry editing
* Form entry deletion
* Form entry details view with resolved selected cars
* Client-side session list of created entries
* Global API error handling
* Seeded startup data for easier first run

## Testing

```
src/test
	├── java
	│   └── com
	│       └── alaasmagi
	│           └── car_brands_api
	│               ├── ApplicationTest.java
	│               ├── application
	│               │   ├── CarServiceTest.java
	│               │   └── FormEntryServiceTest.java
	│               ├── data_access
	│               │   ├── CarRepositoryTest.java
	│               │   └── FormEntryRepositoryTest.java
	│               ├── dto
	│               │   └── web
	│               │       └── dto
	│               │           └── FormEntryRequestDtoValidationTest.java
	│               └── web
	│                   └── controller
	│                       ├── CarControllerIntegrationTest.java
	│                       └── FormEntryControllerIntegrationTest.java
	└── manual api tests
		├── opencollection.yml
		├── cars
		└── form-entries
```

Current backend test coverage is 97% (line + branch combined report in Gradle/JaCoCo reports).
<img width="1222" height="343" alt="image" src="https://github.com/user-attachments/assets/661e8b54-a6b7-4ab7-9b8a-8415b57d05b5" />


### Unit tests
Unit tests focus on service and repository behavior to verify business rules and persistence correctness in isolation. Service tests cover core CRUD scenarios and edge cases for cars and form entries. Repository tests validate mapping and storage behavior, including entity relations.

### Validation tests
Dedicated DTO validation tests ensure that incoming request payloads for form entries respect required constraints and fail fast on invalid input.

### Integration tests
Controller integration tests verify HTTP layer behavior end-to-end (request handling, status codes, serialization, and integration with service/repository layers).

### Manual API tests (via Bruno)
Manual API tests are provided in `/car-brands-api/src/test/manual api tests`, covering both `cars` and `form-entries` endpoints via an OpenCollection setup.

## Visuals

### Home view
<img width="1728" height="1117" alt="image" src="https://github.com/user-attachments/assets/c7fb6bbc-3599-4e1f-ab0e-8412d20bf958" />

### Create view
<img width="1728" height="1117" alt="image" src="https://github.com/user-attachments/assets/4b1b8319-29c7-4c9f-8f31-c888f549dad3" />

### Details view
<img width="1728" height="1117" alt="image" src="https://github.com/user-attachments/assets/1ce34f83-211a-4320-98b2-5c6291b6dba6" />

### Edit view
<img width="1728" height="1117" alt="image" src="https://github.com/user-attachments/assets/5a21ce42-002e-4f01-938a-bf5702cafefb" />


## Improvements & scaling possibilities

### Authentication and authorization
Introduce proper user accounts and role-based access (for example admin/editor/viewer) using JWT and refresh tokens. This would make write operations secure and traceable in multi-user environments.

### Server-side pagination and filtering
As the amount of car and form-entry data grows, API endpoints should support pagination, sorting, and richer filtering to improve performance and user experience.

### Audit trail and versioning
For production-like workflows, add change history for form entries (who changed what and when). This improves observability and supports compliance and rollback scenarios.

## Personal opinion
Although my primary backend stack is C# and ASP.NET, I intentionally explore other languages and frameworks to broaden my understanding and strengthen transferable engineering skills. This project reinforces my view that strong software development is driven more by problem-solving, architecture thinking, and clarity than by one specific technology stack.
