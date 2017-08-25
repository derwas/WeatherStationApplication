#Credit
This project has been developped by Jeremy Michel as part of his internship in 2016 at the Insight Centre for Data Analytics under the supervision of Wassim Derguech.

#First iteration of a weather station project.

###Mandatory
 	- JDK 8: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
 	- JRE 8: http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html
 	- MongoDB 3.2.8: https://www.mongodb.com/download-center?jmp=nav#community

###Optional
 	- Robomongo (great MongoDB UI) https://robomongo.org/download
 	- Integration to an IDE: https://www.playframework.com/documentation/2.5.x/IDE

###Included
 	- Java Play 2.5.3
 	- Mongo java driver 3.2.2


##HOW TO RUN:

1. Clone or unzip this repository
2. Run MongoDB server
3. Go to root folderh
4. Run the activator ("bin/activator run") with your console
5. When all assets have been downloaded and the project is compiled, access http://localhost:9000/landing/generator on your web browser (Chrome works 100%, other browsers not tested). Tis will generate a database called "myDB", which contains random sets of data
6. Access http://localhost:9000/landing when the data has been generated
