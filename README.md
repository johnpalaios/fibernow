# Software Engineering Academy by CodeHub - Final Project
  This is the final project developed for CodeHub's Software Engineering Academy 2022 powered by Advantage FSE.
  
  The project is a Web Application for a telecommunication infrastructure company which enables the employees - managers of the company to have access to various information concerning customers, applications of new installations and reports of problems. The applications and reports are tagged with a single-name ticket. Also, the Web Application enables the customers of the company to oversee the status of their ticket.

* The [Requirements Specification Document](https://github.com/johnpalaios/project-telco/blob/main/requirements-specification-doc.pdf)
* A [quick project presentation](https://docs.google.com/presentation/d/1gqNR42QPxQggLjwT2verd79VSpDxyAlU/edit?usp=sharing&ouid=113231103947982569976&rtpof=true&sd=true)
## Description
You can log in to app as an Administrator or as a Customer. In the initialization of the app, an Admin (username & password = admin) is created while no customers are created. An Administrator can only be manually created through the code, while Customers can be created from the UI (only from an Administrator). When you login into the app, we keep in your browser's local storage your credentials and in every API call, the backend validates if you can access each page.

As an Administrator: 
* In your Home Page you can see the next 10 Pending Tickets (out of every customer).
* In the Customers Page you can search Customers based on the Tax ID Number and their Email, while at page load you can see every customer that exists.
* In the Tickets Page, you can search Tickets based on Customer ID and their creation date while also you can create tickets for a specific customer.

As a Customer:
* In the Tickets Page, you can find all your tickets and you search them by their dates of creation
* In the Details Page, you can view and edit your customer details.

The frontend and backend communicate via a REST API which sends the data in JSON format.
The data are stored in a MySQL database.

## User Interface
### Login Page
Implemented a login functionality

![image](https://user-images.githubusercontent.com/59118861/211810672-4189a45a-157c-4546-93cd-1459f265de33.png)
### Example Page (Search Customers as an Admin)
![image](https://user-images.githubusercontent.com/59118861/211810433-551437c3-aa28-41ab-a467-d94a39e51b96.png)

## Technologies Used
### Frontend
* Javascript ES6
* Bootstrap CSS
* Figma (for the prototypes)
* Canva for the logo

### Backend
* Java SE 11 with Jakarta EE framework with the Hibernate JPA
* Intellij IDEA IDE
* Application Server WildFly 25.0.1
* MySQL Server

### Other
* Postman for endpoint testing
* Git and Github for version control

## Contributors
* [Leonardos Stavropoulos](https://github.com/LeoStavropoulos)
* [Lefteris Kostakis](https://github.com/terrys48)
* [Ioannis Palaios](https://github.com/johnpalaios)
