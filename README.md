# CSCI 310 Project 1

## Info

This repository contains the source code and unit tests for Project 1 of the Spring 2019 section of CSCI 310 at USC. Branches of work are created using the pattern `/feature/<FIRST_NAME>-<BRANCH_NAME>`, e.g. `/feature/ankur-setting-up-controllers`

To run this you'll need to have Maven installed, which can be done by running `brew install maven` in the terminal assuming you have HomeBrew installed.

## Build Instructions

0. Ensure Maven is installed. You should be able to do this with a `brew install maven` as long as you have HomeBrew installed. Goolge around for htis.
1. Navigate to the root directory.
1. Run `./mvnw spring-boot:run` to compile and execute the program (NOTE: You might need to run a `./mvnw clean package` prior to running the actual application).
1. Visit the server at `localhost:8080`. To test if it's working, visit (or send a GET request via Postman to) `localhost:8080/test`. You should get the string `Looks like you're up and running!` back.
1. Good luck working!

## Cucumber Testing Setup

1. Navigate to `cucumber/features/dir/constants.rb
2. Replace the file path to match that of your machine

## Quick Notes

1. The entry point of this application is `Application.java` in `src/main/hava/hello`.
2. The routes and function calls exist in `Controller.java` in `src/main/hava/hello`.
3. Most work that needs to be done has been labeled with a `TODO:` tag -- searching for this might make your life easier.

## Team Members:

- William Borie
- Ankur Rastogi
- David Tupper
- Wayne Yu
- Sally Zuo

### Quick Guides

- What is REST? https://medium.com/@lazlojuly/what-is-a-restful-api-fabb8dc2afeb
- Basic REST App in Spring: https://spring.io/guides/gs/rest-service/
- Multiple RequestParams in a controller: https://www.baeldung.com/spring-request-param
