# Read Me First

This project uses Gradle and Spring Boot to build and package the application.

* To build the application use: ./gradlew clean build
* To run the application use: ./gradlew bootRun

You will need at least JDK 11 to compile the code.

I'm using [lombok](https://projectlombok.org/) please refer to the documentation to properly set up your IDE to make usre the code compiles correctly.

I'm also using [jmockit](http://jmockit.github.io/) pleade refer to the documentation to properlt set up your IDE to be able to run the unit test.

The application also includes Swagger to provide an easy way to get documentation for the REST API as well test it. You could find the [Swagger UI](http://localhost:8080/swagger-ui.html)

I'm also using [flyway](https://flywaydb.org/) to initialize the database as well as insert initial data.