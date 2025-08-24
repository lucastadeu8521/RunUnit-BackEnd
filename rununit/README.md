<div align="center">

<h1 align="center">Quality Air Monitoring System</h1>

<p align="center">
    <strong>A system for monitoring and predicting air quality</strong>
</p>

[![pt-BR](https://img.shields.io/badge/lang-pt--BR-green.svg)](./docs/README.pt-br.md)
[![en](https://img.shields.io/badge/lang-en-red.svg)](./README.md)

</div>

## Table of Contents

- [Table of Contents](#table-of-contents)
- [About](#about)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Contributing](#contributing)
    - [Connect with us on LinkedIn](#connect-with-us-on-linkedin)
    - [Fork and clone the repository](#fork-and-clone-the-repository)
    - [Project Structure](#project-structure)
- [Build and Run with Docker Compose](#build-and-run-with-docker-compose)
    - [Prerequisites](#prerequisites)
    - [Building and Starting the Application](#building-and-starting-the-application)
- [See rendered notebook](#see-rendered-notebook)
- [License](#license)

## About
**RUNIT** is a platform for university students designed to encourage running through challenges, performance tracking, and social events.

This **back-end service** is the central component of the RUNIT platform. It's being developed as a robust API that serves as the core hub for business logic and data. The service will manage the main application data and will also connect with an external **AI and Machine Learning back-end**, which will handle advanced data processing and analytics.
## Features
As a Minimum Viable Product (MVP), the back-end currently provides the following core features:

- **Authentication:** Secure user authentication and authorization.
- **User Management:** Endpoints for user profile management.
- **Race Management:** CRUD (Create, Read, Update, Delete) operations for race and event data.
- **Docker Compose Orchestration:** Simplifies setting up and running all services in an isolated environment.
- **Filtered Search:** Advanced search capabilities for races with multiple filtering options.

## Technologies Used

- **Backend:**
    - **Java 21 LTS:** The core language for the back-end application.
    - **Spring Boot 3+:** The main framework for building the RESTful API.
    - **Spring Data JPA & Hibernate 6+:** For data manipulation and using the `.pkl` model.
    - **H2:** The primary relational database used for MVP.

- **Infrastructure:**
    - **Docker:** For containerization of services.
    - **Docker Compose:** For orchestration and management of multiple containers.

## Contributing
We welcome contributions! If you'd like to help improve this project, please follow the guidelines below.

### Connect with us on LinkedIn

1. Connect with Caio Brayner [LinkedIn](https://www.linkedin.com/in/caiogomesbrayner).
2. Connect with Francisco Primo [LinkedIn](http://www.linkedin.com/in/franciscoprimo).
3. Connect with Nicole Karoliny [LinkedIn](https://www.linkedin.com/in/nicole-karoliny-0bbb41238).

### Fork and clone the repository

1. Fork the repository [(click here to fork now)](https://github.com/Caio-GBrayner/RunUnit-BackEnd)
2. Clone your fork: `git clone https://github.com/Caio-GBrayner/RunUnit-BackEnd`
3. Create a new branch for your changes: `git checkout -b feature/my-new-feature`
4. Push your commits: `git commit -m "feat: Adds new feature"`
5. Push your changes to your fork: `git push origin feature/my-new-feature`
6. Submit a new Pull Request to the main repository.

### Project Structure
The project structure is organized as follows:
```
    RunUnit-BackEnd/
    ├── rununit/                  # Contains the Flask API code and ML model
    │   ├── src/                # Flask application
    │   ├── gradle/wrapper
    │   ├── settings.gradlew
    │   ├── build.gradle
    │   ├── gradlew.bat
    │   ├── gradlew
    │   └── Dockerfile            # Dockerfile for the backend service
    ├── docs/                     # documents
    ├── .dockerignore
    ├── .editorconfig
    ├── .gitattributes       
    ├── LICENSE        # Contains static frontend files and Nginx configuration
    ├── docker-compose.yml        # Docker services orchestration
    └── README.md 
```
## Build and Run with Docker Compose

To get the system up and running, you'll need Docker, Docker Compose and JDK 21 LTS installed.

### Prerequisites

1. Make sure you have the following tools installed on your machine:

    - **Docker Engine:** [Installation instructions](https://docs.docker.com/engine/install/)
    - **Docker Compose:** Usually comes bundled with Docker Desktop. If not, [install it separately.](https://docs.docker.com/compose/install/)
    - **JDK 21 LTS:** The project requires Java Development Kit 21 to be installed on your machine to build the application before containerization, [install it separately.](https://www.azul.com/downloads/?version=java-21-lts&package=jdk#zulu)

### Building and Starting the Application
Follow these steps to build the images and start the services:

1. **Navigate to the project's root directory** in your terminal (where `gradlew is located`):

```bash
    cd /path/to/RunUnit-BackEnd/rununit
```

2. **Build the application with Gradle:**

First, you need to package the application into a JAR file. Navigate to the project's root directory in your terminal and execute the Gradle build command:

```bash
    ./gradlew build
```

4. **Build the images and start the containers:**

Navigate to the project's root directory** in your terminal (where `docker-compose.yml is located`):

```bash
    cd /path/to/RunUnit-BackEnd/
```

After the build is complete, you can start the back-end

```bash
    docker compose up --build
```
- The `--build` command will ensure the images are built (or rebuilt if there are changes in the Dockerfiles).
- This process may take a few minutes the first time, as Docker will download base images and install dependencies.

3. **Access the application:**
   Once the containers are running, you can access the frontend in your browser at:

- Open a tool like Postman or your browser and navigate to the API's main endpoint:

`http://localhost:8080/races`

- A successful response will return a JSON array, indicating the service is running and connected to the database.

4. **Stop the application:**
   To stop and remove containers (but keep the built images), use:

```bash
    docker compose down 
```
To stop and remove containers, networks, and images (for a complete cleanup or rebuilding from scratch):

```bash
  docker compose down --volumes --rmi all  
```

## License

This project is licensed under the MIT License. See the [LICENSE](./LICENSE) file for more details.