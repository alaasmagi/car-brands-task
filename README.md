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

