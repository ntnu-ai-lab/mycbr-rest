
# Table of Contents

1.  [Introduction](#introduction)
2.  [How to](#how-to)
3.  [Functionalities](#functionalities)
4.  [Customization to other myCBR projects](#customization-to-other-mycbr-projects)
5.  [REST API](#org86506e3)


<a id="introduction"></a>

# Introduction

This project puts myCBR into Spring.io and provides the created REST
calls through a Swagger API


<a id="how-to"></a>

# How to

-   First clone the project
-   To build it go into its root folder and run:

    mvn clean install

-   In order to deploy the Spring app, run:

    java -jar ./target/mycbr-rest-example-1.0-SNAPSHOT.jar 

-   After Spring has started, you can find the API documentation here:
    <http://localhost:8080/swagger-ui.html#!/>
-   Greeting controller is the sample I used to build the app
-   CBR Controller contains the myCBR bits
-   ctrl + C shuts down the server


<a id="functionalities"></a>

# Functionalities

-   The goal is to provide the entire retrieval, however, so far only the
    model is working ## GET Requests
-   /case provides the case content
-   /concepts provides all concept names in the project
-   /casebase provides the name(s) of the case bases associated with the
    project
-   /retieval provides the similarity-based retrieval either by specifying
    symbols or an id of existing cases
-   /attributes provides a list of attributes and their value types
-   /values provides the list of allowed values for SymbolDesc attributes
    and min/max for IntegerDesc/DoubleDesc/FloatDesc ## POST Requests
-   /retrieval allows you to post a query with a number of attributes,
    e.g.:  
    `{"Doors": 4, "Model": "e_300_diesel", "Manufacturer": "mercedes-benz","Color":"yellow, blue"}`
-   Currently only Symbol, Integer, Double and Float attributes are
    supported.
-   Currently only Multiple Symbol attributes are supported
-   the number of returned cases can be set, use -1 for returning all


<a id="customization-to-other-mycbr-projects"></a>

# Customization to other myCBR projects

-   The app requires a myCBR project, which should be put into
    mycbr-rest-example/src/main/resources
-   In order to detect the project file, CBREngine.java has to be adapted:
-   private static String projectName = "used\\<sub>cars</sub>\\<sub>flat.prj</sub>" should be
    changed to the project file's name
-   The rest of the API is independent from the project, hence only the
    parametebrs such as case base and comcept names have to be adapted to
    the new project


<a id="org86506e3"></a>

# REST API

    /instances
    /casebases
    /concepts
    /concepts/{conceptID}/attribute
    /concepts/{conceptID}/attribute/{attributeID}
    /concepts/{conceptID}/attribute/{attributeID}/similarityFunctions
    /concepts/{conceptID}/attribute/{attributeID}/similarityFunctions/{similarityFunctionID}
    /concepts/{conceptID}/instances

Semantically one can PUT and DELETE to each of these endpoints, such that a HTTP DELETE to "<http://host:port/casbases>" would delete all casebases.
Each PUT operation has it's own associated JSON format, e.g. for describing a instances or a similarity function of a concept or attribute.
Below we describe the JSON format for each of the PUT operations.

