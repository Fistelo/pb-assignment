# Productboard interview technical task

## How to run the project
PostgreSQL database with configuration matching the application specs can be found in docker-compose.yml. 
`docker-compose up database` command will run it on 5432 port.

The Spring Boot application can be run by using command `mvn spring-boot:run`.
The server is starting on port 8080.

## Test API manually

For manual testing purposes project contains a postman collection of available requests.
Note that the Github fetching logic, which is scheduled for once a day can also be access through API endpoint to make it easier to test.