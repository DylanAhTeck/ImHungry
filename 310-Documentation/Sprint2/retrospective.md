# Sprint Retrospective 
Specify date, time, location, and participants of this meeting. Description of outcomes of the meeting.

## Logistics
Date: April 3rd, 2019
Time: 10:00AM
Location: JFF 333
Participants: Andy, Wayne, Ankur, Chris

## What will be done to fix the sprint report?
The main area we need to improve on is documenting the pair programming sessions and performing/documenting our test driven development. In the beginning of Sprint 1, some members were not informed that TDD was required and resulted in us losing some points there. During the retrospective, we all came to agreement that we must write test code before implementation code. To make sure we will do this, we will include writing test code in our sprint backlog. Something we also agreed to do is to double check that all links for pictures and commits are working before the document is submitted. To fix the points lost on sustainable pace, we have scheduled more scrums and pair programming sessions during the first week and weekend of the sprint. We believe that if we are more diligent during Sprint 2, we will make up for the points we lost during the Sprint 1 report.

## What will be done to fix features?

### Feature 1
Upon loading of pages apart from the login and register page, we will check for a logged in user via Firebase as well as a user token in session. If either is missing, the signUserOut endpoint on  the server side will be called and the user will be redirected to the login page. In terms of SSL, we will host the pages on a Github pages domain, which will then enable access via HTTPS.

### Feature 2
The database is already immediately upon search queries or adding items to the predefined lists. We need to make sure we maintain persistence across all web pages and instances. To do this, we will make sure to check if the user is stored in session on all web pages (ex. /managelist) and if there is no user in session make them login, if there is pull their data from the database. We also need to make sure when a user signs out that we clear the server-side data stored for the predefined lists.

### Feature 4
We will create a new div allocated for the prior searches on the bottom of the results page. Upon page load, the prior searches from the user will be retrieved and compiled into clickable links with the query word displayed alongside a mini collage. Each of these links should reload the results page and display the results pertaining to that specific query. Cucumber tests should be created to ensure the storage of these prior searches and correct display.

### Feature 5
We will replace the current whitesmoke background with an aesthetic image, and select a color scheme that persists throughout each page. Every button will be styled with color, and will remain in place while dynamic content is generated. We will also modify the collage section to display a loading message until the page content has fully loaded.

### Feature 8
We will change the radius input to be in miles instead of meters, and perform conversion to meters on server side for API calls. An error message will also be implemented when there exists no relevant restaurant within the radius constraints.
