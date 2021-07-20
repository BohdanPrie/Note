# Note <image src="/Note/src/main/webapp/pictures/page.png"  width="40"> [Webpage](http://vps-37630.vps-default-host.net/main) 
Note is a REST java web project,  that uses CRUD operations to work with user's data in Database.     
Project built to help people keep their plans and thoughts in a place to which they can access at any moment at any place in the world,
## Technologies
Architecture: 3 tier
Technology | Used
| --- | --- |
Database | postgresql
Web | Servlet API
Work with Database | Plain JDBC
Build | Maven
Server | Tomcat

## Libraries
Library | Used
| --- | --- |
Testing | Junit
Logging | org.apache.logging.log4j (version 2)
JSON | com.fasterxml.jackson.core

Project is mobile supported
## Installation
To install this project on your machine follow these steps:
1) Install java 8 or higher
2) Install maven 
3) Install git or just download this project as .zip
4) Install postgresql and execute [database.sql](/Note/src/main/resources/database.sql)
5) Change parameters in [context.xml](/Note/src/main/tomcatconf/context.xml)
````
username="your_username"  // if you didn't change your_username, set default value (postgres)
password="your_password" 
url="jdbc:postgresql://localhost:5432/your_database_name"/> // if you didn't change your_database_name, set default value (notesapp)
````
6) Execute next command: **mvn install tomcat7:run**    

----
## Documentation
The source code is fully documented, so it won't be a problem to understand what which method or class is responsible for 

----
## Future plans:
- [ ] Rewrite this project using Spring
- [ ] Add Hibernate
- [ ] Add security
- [ ] Add data hashing
- [ ] Add mockito testing
- [ ] Add feature (Task with time)
- [ ] Change startPage mapping from ***/main*** to ***/***