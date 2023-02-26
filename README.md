# Codenemy Backend

The Codenemy backend is responsible for managing the core functionality of the platform, including user authentication, code compilation, and challenge management. This README provides an overview of the backend, its architecture, and how to get started contributing to the project.

## Technologies Used

The Codenemy backend is built using the following technologies:

- [Spring Boot](https://spring.io/projects/spring-boot) - a popular Java-based framework for building web applications.
- [Spring MVC](https://spring.io/guides/gs/serving-web-content/) - a module within Spring Boot for building web applications.
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa) - a module within Spring Boot for integrating with relational databases using the Java Persistence API (JPA).
- [MySQL](https://www.mysql.com/) - a popular open-source relational database management system.
- [Maven](https://maven.apache.org/) - a build automation tool used for managing dependencies and building the project.

## Project Structure

The backend codebase is structured as follows:

- `src/main/java/com/codenemy/backend/` - the main package containing the Java classes for the backend application.
  - `ApiApplication.java` - the entry point of the application that starts the Spring Boot server.
  - `util/` - contains the utility classes for the application.
  - `controller/` - contains the controllers for handling user authentication, challenge management, and code compilation.
  - `model/` - contains the model classes for defining the MySQL schema.
  - `repository/` - contains the repository interfaces for accessing MySQL data.
  - `service/` - contains the service classes for implementing the business logic of the application.
- `src/main/resources/` - contains the application properties files.

## Getting Started

To get started with the Codenemy backend, follow these steps:

1. Clone the repository: `git clone https://github.com/ChaudharySamirZafar/codenemy-backend.git`.
2. Install the dependencies: `mvn install`.
3. Create a MySQL database and configure the application.properties file with the correct database URL, username, and password.
4. Start the server: `mvn spring-boot:run`.

## Contributing

We welcome contributions to the Codenemy backend! To contribute:

1. Fork the repository.
2. Create a new branch for your feature/bugfix: `git checkout -b feature/<feature-name>`.
3. Make your changes and commit them: `git commit -m "Description of your changes."`.
4. Push your changes to your fork: `git push origin feature/<feature-name>`.
5. Open a pull request on the main repository and describe your changes.

## License

The Codenemy backend is released under the [MIT License](https://opensource.org/licenses/MIT).
