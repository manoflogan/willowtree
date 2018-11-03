# Restful Webservice to allow the users to play a quiz to identify your colleagues.

## Table of Contents

- [Assignment](Assignment.md)
- [Requirements](#requirements)
- [Setup](#setup)
- [Deployment](#deployment)
- [API Endpoints](#api-endpoints)
  * [Register Quiz](#register-quiz)
      + [Register Success Fields](#success-response-fields)
  * [Identify Profile From Among Headshots](#identify-profile-from-among-headshots)
      + [Success Response Fields](#success-response-fields)
      + [Error Response Fields](#error-response-fields)
         * [User Profiles Not Found](#user-profiles-not-found)
  * [Play Quiz](#play-the-quiz)
      + [Success Response Fields](#success-response-fields-2)
      + [Error Response Fields](#error-response-fields-1)
         * [Quiz Not Found](#quiz-not-found)
         * [Question Not Found](#question-not-found)
         * [Question Already Answered](#question-already-answered-correctly)
         * [Quiz Already Ended](#quiz-ended)
  * [Get Score](#get-score)
      + [Success Response Fields](#success-response-fields-3)
      + [Error Response Fields](#error-response-fields-2)
         * [Quiz Not Found](#quiz-not-found-1)


# Requirements

* Java 8

* Maven 3.x

# Setup

* Clone the repository.

* Execute `cd willowtree`

# Deployment

There are two ways to deploy the web application.

As this is a Maven project, it can be imported in an IDE such as Eclipse or IntelliJ as a Maven Project.
Create a Run configuration -> Java Application. Once done, select App as a main-class. The application will be deployed to an embedded TOMCAT container

Build and compile the project from command line. Navigate to the project root using the command line, and execute the following command `mvn spring-boot:run`. You will need maven plugin for that.

The RESTful services can be invoked after either steps is performed.

IMPORTANT: these two instructions are mutually exclusive.


# API Endpoints

## Register Quiz

`POST /willowtree/quiz`

Registers a quiz for the contestant and assigns a unique alphanumeric string to the contestant in response.

### Success Response Fields ###
A response status code will be 201 Created and will have the following characteristics.

| Name | Type | Description | Read only |
| :---         |     :---:      |          :--- |      :---:      |
| quizId  | string | Unique quiz id |true
| created | timestamp | UTC timestamp |true

The sample response is given below:

```
{
    "quizId": "MYCjFlD8Rc9dzu5W",
    "created": "2018-10-13T10:12:30.084"
}
```

## Identify Profile from among headshots.

`GET /willowtree/quiz/<quizId>/identifyfromsixjson`

Returns question in which 6 headshots are returned, and the user is asked to select the headshot of the profile to be identified.

The path variable `quizId` represents the unique quiz identifier.

### Success Response Fields

A response status code will be 200 OK. The success response encapsulates a unique question id that is used to play the quiz game.


| Name | Type | Description | Read only |
| :---         |     :---:      |          :--- |      :---:      |
| quizId  | string | Unique quiz id passed as a path variable |true|
| questionId  | long | Unique question |true|
| questionText | string | Question text|true|
| images | array | array of head shots |true|


The `images` array consists of maximum of 6 entries in which each entry contains the following

| Name | Type | Description | Read only |
| :---         |     :---:      |          :--- |      :---:      |
| imageUrl  | string | url of the image asset |true|
| height  | int | height of the image |false|
| width | int | width of the image|false|
| id | string | unique image identifier that may be an answer of to quiz question |true|


The sample response is given below.

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

### Error Response Fields

The error response encapsulates an array of errors.

| Name | Type | Description | Read only |
| :---         |     :---:      |          :--- |      :---:      |
| errors  | array | array of error objects |true |

where each error object consists of the following data sets.

| Name | Type | Description | Read only |
| :---         |     :---:      |          :--- |      :---:      |
| error code  | number | error code indicating the kind of error|true |
| error message  | string | user friendly message |true |

Some of the sample responses are given below

#### User Profiles Not Found

This will return a 404 error if no record for the quiz was found in the database.

```
{
    "errors": [
        {
          "error_code": 404,
          "error_message" : "No user profiles found for quiz id <quiz id>."
        }
    ]
}
```

### Play the quiz

`POST /willowtree/quiz/<quiz_id>/question/<question_id>`

The endpoint expects a request body that will encapsulate the answer to the question asked in the previous section.

The path variables are given below.

| Name | Type | Description | Read only |
| :---         |     :---:      |          :--- |      :---:      |
| quiz_id  | string | Unique quiz id passed as a path variable |true|
| question_id | long | unique question id. This can be obtained from the response body|true|

The request body encapsulates the user provided response, and its body is given below

| Name | Type | Description | Read only |
| :---         |     :---:      |          :--- |      :---:      |
| quizId  | string | Unique quiz id  |true|
| questionId | long | unique question id; an example of which is passed in the response body in the previous section|true|
| id | string | unique headshot id |true|

The sample request is given below

```
curl -XPOST -H "Content-type: application/json" -d '{"id": "<headshot_id>", "quizId": "<quizId>", "questionId": "<questionId>"}' 'http://localhost:8080/willowtree/quiz/{{quizId}}/question/{{questionId}}'
```


#### Success Response Fields

If a valid response body has been returned, then the response body has the following format below.

| Name | Type | Description | Read only |
| :---         |     :---:      |          :--- |      :---:      |
| quizId  | string | Unique quiz id  |true|
| questionId | long | unique question id; an example of which is passed in the response body in the previous section|true|
| correctAnswer | boolean | true/false |true|
| playerAnswer | string | user provided input used to verify the headshot id |true|

The sample response is given below:

```
{
    "quizId": "711a27201fd841418c717771f789da00",
    "questionId": 175,
    "isCorrect": true,
    "playerAnswer": "2JbFih4rzyK8E0eIUmUCeo"
}
```

#### Error Response Fields

The error response body represents an error of errors returned as 200 OK

| Name | Type | Description | Read only |
| :---         |     :---:      |          :--- |      :---:      |
| errors  | array | array of error objects |true |

where each error object consists of the following fields

| Name | Type | Description | Read only |
| :---         |     :---:      |          :--- |      :---:      |
| error code  | number | error code indicating the kind of error|true |
| error message  | string | user friendly message |true |

Some of the sample responses are given below

##### Quiz Not Found

```
{
    "errors": [
        {
            "error_code": 404,
            "error_message": "Quiz <quiz_id> was not found."
        }
    ]
}
```


##### Question Not Found

```
{
    "errors": [
        {
            "error_code": 404,
            "error_message": "Question <question_id> was asked for the quiz id <quiz_id>."
        }
    ]
}
```

##### Question Already Answered Correctly

```
{
    "errors": [
        {
            "error_code": 404,
            "error_message": "Question {0} has already been answered correctly."
        }
    ]
}
```

##### Quiz Ended

```
{
    "errors": [
        {
            "error_code": 400,
            "error_message": "Quiz <quiz id> has already ended."
        }
    ]
}
```

### Get Score

`GET /willowtree/quiz/<quizId>/score`

Returns the score for the quiz game.

#### Success Response Fields</a>

| Name | Type | Description | Read only |
| :---         |     :---:      |          :--- |      :---:      |
| quizId  | string | Unique quiz id passed as a path variable |true|
| score  | int | Quiz Score |true|

#### Error Response Fields</a>

The error response body represents an error of errors returned as 200 OK

| Name | Type | Description | Read only |
| :---         |     :---:      |          :--- |      :---:      |
| errors  | array | array of error objects |true |

where each error object consists of the following fields

| Name | Type | Description | Read only |
| :---         |     :---:      |          :--- |      :---:      |
| error code  | number | error code indicating the kind of error|true |
| error message  | string | user friendly message |true |

Some of the sample responses are given below

##### Quiz Not Found

```
{
    "errors": [
        {
            "error_code": 404,
            "error_message": "Quiz <quiz_id> was not found."
        }
    ]
}
```
