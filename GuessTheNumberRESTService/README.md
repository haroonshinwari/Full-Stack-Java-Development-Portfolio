<h1>Guess the Number REST Service</h1>

Guess the Number REST Service is a Spring Boot REST application created using JDBC Template, to access an SQL database.The application creates, retrieves, updates, and deletes data from the database to allow the user to play a simple number-guessing game.

Using the appropriate GET and POST methods in the Postman application, the user can create games, make guesses, receive feedback, and view correct solutions upon completion of games. The code itself contains the required database configuration, as well as all game logic.

This was the first project in the second half of the course introducing JDBC Template as a new form of data persistence and SpringBoot to establish and automatically configure application components. Additionally we were introduced to the REST architecture. Completing this project has supported me in developing my ability to:


- Run an application as a SpringBoot application and have the dependencies injected with annotations
- Ensure JDBC is configured correctly to carry out the CRUD operations
- Test the DAO layer and creating a seperate test database 
- Use Postman to carry out GET and POST requests and ensuring all game rules are tested and verified
- Use controller handler methods are handled via annotations (bad requests etc)
