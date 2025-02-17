# spring-microservice-case-study
This application consists of Spring application and MySQL database that is dockerized.  
### To run, first execute command 'docker compose up'  
### When DB container is running, start application by running class 'Demo Application'  
App contains 4 endpoints to execute following operations:  
**/register** - registers user with given credentials, returning User's ID and status of operation.  
**/login** - returns JWT token, if users exists in database and credentials are correct.  
This token should be used as Bearer Token in Authorization header.  
**/items (POST)** - creates item with following fields: owner(User's UUID), name - item's name  
**/items (GET)** - fetches all items associated with currently signed-in User.  
Additionally, after 24h Bearer token is expired, which means that User is logged out and have to generate a new token.  