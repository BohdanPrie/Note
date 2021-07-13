# Note <image src="/Note/src/main/webapp/pictures/page.png"  width="40"> [Webpage](http://vps-37630.vps-default-host.net/main) 
Note is java web project, built to help people keep their plans and thoughts in a place to which they can access at any moment at any place in the world
## Technologies
Technology | Used
| --- | --- |
Database | postgresql
Web | Servlet API
Connection to Database | Plain JDBC
Build | Maven
Server | Tomcat

## Libraries
Library | Used
| --- | --- |
Testing | Junit
Logging | org.apache.logging.log4j (version 2)
JSON | com.fasterxml.jackson.core

## Instalation
To install this project on your machine follow these steps:
1) Install java 8 or higher
2) Install maven 
3) Install git or just download this project as .zip
4) Install postgresql and execute [database.sql](/Note/src/main/resources/database.sql)
5) Change parameters in [context.xml](/Note/src/main/tomcatconf/context.xml)
````
username="your_username"  // if you didn't change your_username, set default value (postgres)
password="your_password" 
url="jdbc:postgresql://localhost:5432/your_database_name"/>
````
6) Execute next command: **mvn tomcat7:run**    

----
## Documentation
The source code is fully documented, so it won't be a problem to understand what which method or class is responsible for 

----
## Future plans:
- [ ] Rewrite this project using Spring
- [ ] Add Hibernate
- [ ] Add security
- [ ] Add data hashing
- [ ] Add feature (Task with time)