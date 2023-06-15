# User LoginRegistration form using JWT Authentication
 This projects implements a user login registration Rest API using SpringBoot technology. The security to access the secured RestAPI in the project is provided by JWT token authentication. The JUnit testing and RestAssured API automation is done for testing the application.

## Technology Used
1. Spring Boot 
2. JWT
3. Sprint Data JPA
4. Hybernate
5. Mysql
6. JUnit and Mockito
7. Rest Assured
8. Java

## Getting Started

To get started with this project, you will need to have the following installed on your local machine:
JDK 17+
Maven 3+
MySQL
Text Editor or Integrated Development Environment (IDE)

To build and run the project, follow these steps:

1. Clone the repository: git clone https://github.com/Sinchanacy/Backend--userRegistrationLoginForm.git
2. Navigate to the project directory: cd JWTSpringBoot
3. Create a MySQL database : 
    $ create database regtokens  and 
   change MySQL username and password as per your MySQL installation
4.Edit spring.datasource.username and spring.datasource.password properties as per your mysql installation in src/main/resources/application.properties and provide the database connection.
5. Build the project: mvn clean install
6. Run the project: mvn spring-boot:run

## API

### 1. Registering a User :
curl --location --request POST 'localhost:8080/welcome/register' \
--header 'Content-Type: application/json' \
--data-raw '{
    "firstname" : "....",
    "email": "....",
    "password": "...."
}'                                                                                                                                        
Upon successfull registration a response is returned , if the user tries to register with an existing email an exception will be thrown.

### 2. Verify Registration :
curl --location --request GET 'localhost:8080/welcome/verifyReg?token={VerifyRegToken}'                                              
Upon successfull verification a response of User Verified Successfully is returned . If the token is invalid or expired a response of Bad User is returned.

### 3. Authenticating a user :
curl --location --request POST 'localhost:8080/welcome/authenticate' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "....",
    "password": "...."
}'                                                                                                                                   
Upon successfull authentication with valid email and password a JWT token is returned.

### 4. Secured API
curl --location --request GET 'localhost:8080/hello' \
--header 'Authorization: Bearer {JWTToken}'                                       
For valid Jwt token the response of the secured API request is returned.

### 5. Forgot Password Token 
curl --location --request POST 'localhost:8080/resetpassword' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "...."
}'
For valid email the forgot password reset token is sent to the user.

### 6. Save Password
curl --location --request GET 'localhost:8080/savePassword?token={Token}'               
For valid Forgot password reset token , the password is changed successfully along with a response of Password Reset Successfully.

### 7.Change Password 
curl --location --request POST 'localhost:8080/changePassword' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "....",
    "oldPassword": "....",
    "newPassword": "...."
}'               
The password is reset successfully when a valid email and old password is received in the request body.

