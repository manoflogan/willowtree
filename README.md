# Restful Webservice to allow the users to player a bowling game [![Build Status](https://travis-ci.org/manoflogan/DsAndAlgo_Java.png)](https://travis-ci.org/krishnanand/bowling_game)

## Table of Contents

- [Requirements](#requirements)
- [Deployment](#deployment)
- [Setup](#setup)
- [API Endpoints](#api-endpoints)
  * [Register Game](#registergame)
      1. [Registration Success Response](#register-success-response)
  * [Play the game](#play-game)
      1. [Success Response](#success-responses)
      2. [Error](#error-responses)
         * [Game Not Found](#game-not-found-error)
         * [Invalid Scoring Format](#score-format-invalid-error)
         * [Game Not Found](#game-already-played-error)
         * [Two threads attempting to score at the same time](#optimistic-locking-error)
  * [Get Frame Score](#get-frame-score)
      1. [Success Response](#score-success-response)
      2. [Error](#score-error-response)
         * [Game Not Found](#score-game-not-found)


### Requirements ###

* Java 8

* Maven 3.x

### Setup ###

* Clone the repository.

* Execute `cd willowtree`

### Deployment ###

There are two ways to deploy the web application.

As this is a Maven project, it can be imported in an IDE such as Eclipse or IntelliJ as a Maven Project.
Create a Run configuration -> Java Application. Once done, select App as a main-class. The application will be deployed to an embedded TOMCAT container

Build and compile the project from command line. Navigate to the project root using the command line, and execute the following command `mvn spring-boot:run`. You will need maven plugin for that.

The RESTful services can be invoked after either steps is performed.

IMPORTANT: these two instructions are mutually exclusive.


# API Endpoints ###

## <a name="registergame">Register Game</a>

#### POST /willowtree/quiz ####

Registers a bowling game for the contestant and assigns a unique alphanumeric string to the contestant.

#### <a name="register-success-response">Response Body</a> ####

A response body will include:

● a status code of 201 Created

● Response Body Properties

| Name | Type | Description | Read only |
| :---         |     :---:      |          :--- |      :---:      |
| quizId  | string | Unique game id |true
| created | timestamp | UTC timestamp |true


```
{
    "game_id": "MYCjFlD8Rc9dzu5W",
    "created": "2018-10-13T10:12:30.084"
}
```

### <a name="play-game">Play the game.</a> ###

#### GET /willowtree/quiz/<quizId>/identifyfromsixjson ####

Represents a question in which 6 headshots are returned, and the user is asked to select the headshot of the profile to be identified.

Response body after playing a frame is given below:


| Name | Type | Description | Read only |
| :---         |     :---:      |          :--- |      :---:      |
| quizId  | string | Unique game id passed as a path variable |true|
| questionId  | long | Unique question |true|

#### <a name="success-responses">Success Response</a> ####

```
{
    "quizId": "711a27201fd841418c717771f789da00",
    "questionId": 175,
    "questionText": "Which profile among these is Will Ellis?",
    "images": [
        {
            "imageUrl": "//images.ctfassets.net/3cttzl4i3k1h/46AT04lolyu8QcUagwKIik/6ec87624262ffdd755e2e4dde8b8b918/headshot_scott_zetlan.jpg",
            "height": 340,
            "width": 340,
            "id": "46AT04lolyu8QcUagwKIik"
        },
        {
            "imageUrl": "//images.ctfassets.net/3cttzl4i3k1h/1fcq8yhkgOOQc8iS2yo0YK/b186e7f0203f7b8391b6d73e85acb2e5/IMG_1190.jpg",
            "height": 2622,
            "width": 2622,
            "id": "1fcq8yhkgOOQc8iS2yo0YK"
        },
        {
            "imageUrl": "//images.ctfassets.net/3cttzl4i3k1h/6SaAMdVVEQsIMumMAWQ04S/e0efe956903d3eb891470ba79d4804d0/headshot_tesceline_tabilas.jpg",
            "height": 340,
            "width": 340,
            "id": "6SaAMdVVEQsIMumMAWQ04S"
        },
        {
            "imageUrl": "//images.ctfassets.net/3cttzl4i3k1h/64IBagkE0gga82G2W6cWsm/95b43c4a0c549dc33a80f23d4382c4f4/christy.png",
            "height": 664,
            "width": 664,
            "id": "64IBagkE0gga82G2W6cWsm"
        },
        {
            "imageUrl": "//images.ctfassets.net/3cttzl4i3k1h/3lcliXPNFeIaWUA4GUMEO4/13db5b49dc2a746d03dbecbb05501f84/headshot_preston_brown.jpg",
            "height": 340,
            "width": 340,
            "id": "3lcliXPNFeIaWUA4GUMEO4"
        },
        {
            "imageUrl": "//images.ctfassets.net/3cttzl4i3k1h/2JbFih4rzyK8E0eIUmUCeo/b28d41104e6558ec3e12e1845f1ead59/headshot_will_ellis.jpg",
            "height": 340,
            "width": 340,
            "id": "2JbFih4rzyK8E0eIUmUCeo"
        }
    ]
}
```

#### <a name="error-responses">Error Responses</a> ####

The error response is body is an array of errors.

| Name | Type | Description | Read only |
| :---         |     :---:      |          :--- |      :---:      |
| errors  | array | array of error objects |true |

where each error object consists of the following fields

| Name | Type | Description | Read only |
| :---         |     :---:      |          :--- |      :---:      |
| error code  | number | error code indicating the kind of error|true |
| error message  | string | user friendly message |true |

Some of the sample responses are given below

#### <a name="game-not-found-error">1. Game Not Found</a> ####

This will return a 404 error if no record for the game was found in the database.

```
{
    "errors": [
        {
          "error_code": 404,
          "error_message" : "No game was found for the game id: <game_id>."
        }
    ]
}
```

#### <a name="score-format-invalid-error">2. Invalid Scoring Format</a> ####

This will return a 404 error if no record for the game was found in the database.

```
{
    "errors": [
        {
          "error_code": 400,
          "error_message" : "Score format: <score> is invalid."
        }
    ]
}
```

#### <a name="game-already-played-error">3. Game Not Found</a> ####

This will return a 404 error if no record for the game was found in the database.

```
{
    "errors": [
        {
          "error_code": 400,
          "error_message" : "Game:\'<game_id>\' has already been played."
        }
    ]
}
```

#### <a name="optimistic-locking-error">4. Two threads attempting to score at the same time.</a> ####

This will return a 404 error if no record for the game was found in the database.

```
{
    "errors": [
        {
          "error_code": 500,
          "error_message" : "Unable to save score: '<score>' for game: '<game_id>'."
        }
    ]
}
```

### <a name="get-frame-score">Get the current score.</a> ###

#### GET /game/<game_id>/score ####

Returns the score of the game at any given time.

The response body consists of

| Name | Type | Description | Read only |
| :---         |     :---:      |          :--- |      :---:      |
| game_id  | string | Unique game id passed as a path variable |true|
| total_score | int | Total score till the present time|true|

#### <a name="score-success-response">1. Success Response</a>

The sample response is given below.
```
{
    "total_score": 182,
    "game_id": "<game_id>"
}
```

#### <a name="score-success-response">2. Error Response</a> ####
The error response is body is an array of errors.

| Name | Type | Description | Read only |
| :---         |     :---:      |          :--- |      :---:      |
| errors  | array | array of error objects |true |

where each error object consists of the following fields

| Name | Type | Description | Read only |
| :---         |     :---:      |          :--- |      :---:      |
| error code  | number | error code indicating the kind of error|true |
| error message  | string | user friendly message |true |

Some of the sample responses are given below

#### <a name="score-game-not-found">Game Not Found</a> ####

```
{
    "errors": [
        {
            "error_code": 404,
            "error_message": "No game was found for the game id: <game_id>."
        }
    ]
}
```
0
