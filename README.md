# fleet-pulse

**fleet-pulse** – A high-performance, real-time fleet tracking system designed for seamless vehicle monitoring and interactive map visualization.

---

## Tech Stack

The project uses a monorepo structure:

* **Backend:** Java 26, Spring Boot 3.x, JPA/Hibernate, WebSockets (STOMP), Event Handling.
* **Database:** PostgreSQL + PostGIS (for geospatial data).
* **Frontend:** Vue 3 (Vite), TypeScript, Pinia, Axios, Stomp.
* **Build:** Gradle.

---

## Configuration

To run the project, create a `.env` file in the root directory of the project with the following structure:

```env
# Database connection string
DB_URL=jdbc:postgresql://your-db-host:5432/db_name

# Database credentials
DB_USER=your_username
DB_PASSWORD=your_password

# API & WebSocket endpoints
FRONTEND_URL=Backend server URL (e.g., http://localhost:5173)
BACKEND_URL=Backend server URL (e.g., http://localhost:8080)
WEB_SOCKET_URL=WebSocket server URL (e.g., ws://localhost:8080/ws-fleet)
```

## Getting Started
1. Start the Backend
Navigate to the backend/ directory and run:

```
cd backend
./gradlew bootRun
```

2. Start the Frontend
Navigate to the frontend/fleet-client/ directory and run:

```
npm install
npm run dev
```

## Architecture Overview
The system handles data through three primary streams:

1. Real-time Position: Vehicles Current location streamed via WebSockets (STOMP).
2. Notifications: Real-time event handling for Notification updates.
3. Historical Data: REST API endpoints for fetching past route and vehicle information.

The backend follows a strictly layered architecture (Controller, Service, Repository, Mapper, Converter, DTO) to ensure maintainability and scalability.

## Testing
* Backend: The core business logic is covered with comprehensive unit and integration tests. Run them using: ./gradlew test.
* Frontend: Test coverage is currently in the future development phase.