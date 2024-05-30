# starwars-api

## Project Overview
The Starwars API Challenge is a comprehensive API project designed to manage data related to the Star Wars universe. This API supports CRUD (Create, Read, Update, Delete) operations and integrates with a PostgreSQL database using JPA (Java Persistence API). The project includes endpoints for managing characters, planets, and starships, ensuring data consistency and integrity through detailed error handling and validation mechanisms.

### Technologies Used
- Spring Boot
- PostgreSQL
- ndjson
- VS Code

The API includes comprehensive error handling mechanisms to manage various scenarios such as invalid inputs, missing resources, and database errors. Each endpoint returns appropriate HTTP status codes and messages to facilitate debugging and integration.

Detailed Comments and Documentation
All code is well-documented with comments explaining the purpose and functionality of classes, methods, and key code segments. This ensures maintainability and ease of understanding for developers.


## Features I would add or change in the future
- enhance the styling
- add more buttons such as back buttons
- fix the indexing
- add the check homePlanet and check starships feature from the create character to the update character as well


## Demo Video
There is a demo video file included to see a walkthrough of the project on localhost


### Project Structure
├── mvnw

├── mvnw.cmd

├── pom.xml

├── src

├── jsonFiles 

│   ├── main

│   │   ├── java

│   │   │   └── com

│   │   │       └── example

│   │   │           └── projectname

│   │   │               ├── DemoApplication.java

│   │   │               ├── controller

│   │   │               ├── exception

│   │   │               ├── model

│   │   │               ├── repository

│   │   │               └── service

│   │   └── resources

│   │       ├── application.properties

│   │       ├── static

│   │       └── templates

│   └── test

│       └── java

│           └── com

│               └── example

│                   └── project_name

│                       └── DemoApplicationTests.java

└── target


