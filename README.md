# Read Me 

This is Spring Boot Application to create a REST API using Camel for below requirements. 

This dockerized implementation provides RESTful API and apache camel routes for validation against json schemas and integrates with backend service.

### Requirements

*	Expose a Restful Service
*	HTTP verb as POST and Media Type can be JSON or XML
*	Define/Create a front end and backend schema’s (JSON or XML Schema’s) – A simple schema should be sufficient.
*	Validate a Message (using a front end JSON or XML Schema)
*	Transform a Message (Front end JSON or XML format to a backend JSON or XML format)
*	Validate a transformed message (using a backend JSON or XML Schema)
*	Build a Mock Service to receive a message and return some sample response
*	Unit Test
*	Created a test that connects to the API you have created, through any Rest Client, and returns a response
*	One happy path use case (End to End flow)
*	One unhappy path use case (End to End flow)
*	Once the exercise is completed then it would be great to create a Docker file to create an image of the microservice. (It’s optional, but preferred)



# Getting Started

### Pre-requisites

  Install JDK8

  Intall Maven

  Install Docker


## Building & Running  tests
    mvn clean test   #running tests

    mvn clean install 

### Starting the app from command line

To start, run following commandline of the applicaton root directory:

    mvn spring-boot:run

## Running the application From Docker:

 If you are running from Intellij/eclipse ,  you need to download the lombok plugin and enable  annotation processors

 ref : https://projectlombok.org/setup/intellij

## Running the application From Docker:

From root of git checkout run the following commands 
        
        mvn package

        docker build -t camel-spring-boot-rest .

To push to any other registry other than local refer docker push

run the app

    docker run -p 8080:8080 -t spring-boot-camel-rest:latest

## Stopping Container

Follow the commands sequentially

List out the process

      docker ps

  the above commings gives the list of proceses and gives full description
ex :

    CONTAINER ID        IMAGE               COMMAND                CREATED             STATUS              PORTS                    NAMES
    f421cb3f36e8        ad1161db15df        "java -jar /app.jar"   About an hour ago   Up About an hour    0.0.0.0:8080->8080/tcp   epic_brown


command to sto the container

   docker stop  CONTAINER ID      # use the container id from above step
    
    docker stop f421cb3f36e8



#Swagger
  
  http://localhost:8080/springboot-camel-integration/swagger-ui.html  
  
### Calling RestEndpoint:


import  hmrc-test.postman_collection.json from the root into PostMan Rest Client

 it has 3 requests
 
 1: Happy Path
 
 2: FailurePath1 - Invalid turnover
 
 3: FailurePath2 - with Id null
 

    Endpoint : http://localhost:8080/testing/applyFacility
    Method : POST
    Content-Type : application/json

   
   Sample Success Body :
    
    {
     "id":"BBLS0001",
     "turnOver":8000

    }
    
  Should return with HttpStatus Code 201 with following response
  
    {
      "id": "BBLS0001",
      "turnOver": 8000,
      "loanApproved": true,
      "loanAmountApproved": 2000
    }
  
  