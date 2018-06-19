# Credit

This project aims to collect data from a weather station and display it in a user friendly manner. It has been developed initially by Jeremy Michel as part of his internship in 2016 then this branch has been udated by Hela Allani during her internship in 2018 under the supervision of Dr. Wassim Derguech.

#First iteration of a weather station project.

### Mandatory
 	- JDK 8: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
 	- JRE 8: http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html
 	- MongoDB 3.2.8: https://www.mongodb.com/download-center?jmp=nav#community

### Optional
 	- Robomongo (great MongoDB UI) https://robomongo.org/download
 	- Integration to an IDE: https://www.playframework.com/documentation/2.5.x/IDE

### Included
 	- Java Play 2.5.3
 	- Mongo java driver 3.2.2


## HOW TO RUN:

1. Clone or unzip this repository
2. Run MongoDB server
3. Go to root folderh
4. Run the activator ("bin/activator run") with your console
5. First of all this project requires data in mongoB that needs to be collected by https://github.com/derwas/OpenWeatherMapDataCollector
6. When the data has been generated, access http://localhost:9000/landing 
