# Introduction

This project puts myCBR into Spring.io and provides the created REST calls through a Swagger API

# How to

* First clone the project 
* To build it go into its root folder and run: mvn clean install
* In order to deploy the Spring app, run: java -jar ./target/mycbr-rest-example-1.0-SNAPSHOT.jar 
* After Spring has started, you can find the API documentation here: http://localhost:8080/swagger-ui.html#!/
 * Greeting controller is the sample I used to build the app
 * CBR Controller contains the myCBR bits

# Functionalities
* The goal is to provide the entire retrieval, however, so far only the model is working
 * /case provides the case content
 * /concept provides the concept name
 * /casebase provides the name(s) of the case bases associated with the project
 * /retieval provides the similarity-based retrieval either by specifying symbols or an id of existing cases