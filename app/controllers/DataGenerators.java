package controllers;

import java.util.*;
import java.lang.Object;
import play.Logger;

//mongoDB imports
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.DeleteOneModel;
import com.mongodb.client.model.InsertOneModel;
import org.bson.Document;


import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Filters.lte;



public class DataGenerators {

/*
	Useful to get a new database with new 'randomized' data from scratch.

	this generator will 
		0 - drop target database if it exists. This ensures it is empty.
		1 - create and connect to the database
		2 - create new collections, with raw and aggregated suffixes for each sensor
		3 - insert the data following the function's parameter
		4 - create the aggregations
		5 - remove the data older than a week (yes, it's not optimal since I could 
			just make the aggregation of data older than a week without adding it,
			but it is easier this way)

		Note - this is assuming localhost, default port and no authentification
*/
	public void fullGeneratorMongoDB(String databaseName, int timeSpanDay, int pointsPerDay, int daysDatakept) {

		String[] COLLECTIONS = {
			"S001_Temp_raw",
			"S001_Temp_aggregated",
			"S002_Humi_raw",
			"S002_Humi_aggregated",
			"S003_WindSpeed_raw",
			"S003_WindSpeed_aggregated",
			"S004_WindDirection_raw",
			"S004_WindDirection_aggregated",
			"S005_Rain_raw",
			"S005_Rain_aggregated",
			"S006_Air_raw",
			"S006_Air_aggregated",
		};

		//value range for each collection
		int MINTEMP = -15;
		int MAXTEMP = 30;
		int MINHUMI = 80;
		int MAXHUMI = 100;
		int MINWINDSPEED = 0;
		int MAXWINDSPEED = 100;
		int MINWINDDIR = 0;
		int MAXWINDIR = 360;
		int MINRAIN = 0;
		int MAXRAIN = 5;
		int MINAIR = 0;
		int MAXAIR = 100;

		/*0 - drop the database, whether it exists or not to make it a clean state*/
		MongoClient mongoClient = new MongoClient( "localhost" , 27017);
		MongoDatabase database = mongoClient.getDatabase(databaseName);
		database.drop();


		/*1 - create and connect to the database*/
		database = mongoClient.getDatabase(databaseName);


		/*2 - create new collections, with raw and aggregated suffixes for each sensor*/
		MongoCollection<Document> tempRawCollection = database.getCollection(COLLECTIONS[0]);
		MongoCollection<Document> humiRawCollection = database.getCollection(COLLECTIONS[2]);
		MongoCollection<Document> windSRawCollection = database.getCollection(COLLECTIONS[4]);
		MongoCollection<Document> windDRawCollection = database.getCollection(COLLECTIONS[6]);
		MongoCollection<Document> rainRawCollection = database.getCollection(COLLECTIONS[8]);
		MongoCollection<Document> airRawCollection = database.getCollection(COLLECTIONS[10]);
		
		MongoCollection<Document> tempAggregatedCollection = database.getCollection(COLLECTIONS[1]);
		MongoCollection<Document> humiAggregatedCollection = database.getCollection(COLLECTIONS[3]);
		MongoCollection<Document> windSAggregatedCollection = database.getCollection(COLLECTIONS[5]);
		MongoCollection<Document> windDAggregatedCollection = database.getCollection(COLLECTIONS[7]);
		MongoCollection<Document> rainAggregatedCollection = database.getCollection(COLLECTIONS[9]);
		MongoCollection<Document> airAggregatedCollection = database.getCollection(COLLECTIONS[11]);


		/*3 - insert the data following the function's parameter*/
		/*3.1 - get generation starting date in milliseconds (epoch)*/
		Calendar now = Calendar.getInstance(); // get current time
		Calendar nextDay = Calendar.getInstance(); // instanciate new calendar with current time
		nextDay.clear(); // clear the new calendar (which is now 0)
		nextDay.set(
			now.get(Calendar.YEAR),
			now.get(Calendar.MONTH) + 1,
			now.get(Calendar.DAY_OF_MONTH) + 1,
			0,
			0
		); // set the time to next day, midnight
		long nextDayTimestamp = nextDay.getTimeInMillis(); // transform beggining date to tinestamp

		/*3.2 - get milliseconds differentials (between two data point)*/
		int millisecondsOneDay = 86400000; // amount of millis in a day
		long deltaMillis = millisecondsOneDay/pointsPerDay; //delta betweem two values

		/*3.3 - insert data in document, incrementing timestamp each time, timeSpanDay times*/
		//bind raw collections to vars


		

		int numberOfInsert = timeSpanDay * pointsPerDay;
		Random random = new Random();
		//temperature raw insert
		for (long i = 0 ; i < numberOfInsert ; i++) {
			long finalTimestamp = nextDayTimestamp + i * deltaMillis; // every var in this operation MUST be long because java use var type to store result when 
			Document document = new Document("timestamp", finalTimestamp) // prioritizing. ( i * deltaMillis can exceed 32 bits easily on long periods of time)
				.append("value", random.nextInt(MAXTEMP - MINTEMP) + MINTEMP)
				.append("unit", "Degrees Celsius");

			tempRawCollection.insertOne(document);
		}

		//humidity raw insert
		for (long i = 0 ; i < numberOfInsert ; i++) {
			long finalTimestamp = nextDayTimestamp + i * deltaMillis; // every var in this operation MUST be long because java use var type to store result when 
			Document document = new Document("timestamp", finalTimestamp) // prioritizing. ( i * deltaMillis can exceed 32 bits easily on long periods of time)
				.append("value", random.nextInt(MAXHUMI - MINHUMI) + MINHUMI)
				.append("unit", "Percent");

			humiRawCollection.insertOne(document);
		}

		//wind speed raw insert
		for (long i = 0 ; i < numberOfInsert ; i++) {
			long finalTimestamp = nextDayTimestamp + i * deltaMillis; // every var in this operation MUST be long because java use var type to store result when 
			Document document = new Document("timestamp", finalTimestamp) // prioritizing. ( i * deltaMillis can exceed 32 bits easily on long periods of time)
				.append("value", random.nextInt(MAXWINDSPEED - MINWINDSPEED) + MINWINDSPEED)
				.append("unit", "Knots");

			windSRawCollection.insertOne(document);
		}

		//wind direction raw insert
		for (long i = 0 ; i < numberOfInsert ; i++) {
			long finalTimestamp = nextDayTimestamp + i * deltaMillis; // every var in this operation MUST be long because java use var type to store result when 
			Document document = new Document("timestamp", finalTimestamp) // prioritizing. ( i * deltaMillis can exceed 32 bits easily on long periods of time)
				.append("value", random.nextInt(MAXWINDIR - MINWINDDIR) + MINWINDDIR)
				.append("unit", "Degrees");

			windDRawCollection.insertOne(document);
		}		

		//rain raw insert
		for (long i = 0 ; i < numberOfInsert ; i++) {
			long finalTimestamp = nextDayTimestamp + i * deltaMillis; // every var in this operation MUST be long because java use var type to store result when 
			Document document = new Document("timestamp", finalTimestamp) // prioritizing. ( i * deltaMillis can exceed 32 bits easily on long periods of time)
				.append("value", random.nextInt(MAXRAIN - MINRAIN) + MINRAIN)
				.append("unit", "Millimeter");

			rainRawCollection.insertOne(document);
		}

		//air raw insert
		for (long i = 0 ; i < numberOfInsert ; i++) {
			long finalTimestamp = nextDayTimestamp + i * deltaMillis; // every var in this operation MUST be long because java use var type to store result when 
			Document document = new Document("timestamp", finalTimestamp) // prioritizing. ( i * deltaMillis can exceed 32 bits easily on long periods of time)
				.append("value", random.nextInt(MAXAIR - MINAIR) + MINAIR)
				.append("unit", "Percent");

			airRawCollection.insertOne(document);
		}


		/*4 - create the aggregations*/
		//get data older than daysDatakept, and aggregate it
		
		Document doc = tempRawCollection.find().sort(descending("timestamp")).first();
		long timestampLatest = (long) doc.get("timestamp");
		Calendar midnightEarlier = new GregorianCalendar();
		midnightEarlier.clear();
		midnightEarlier.setTimeInMillis(timestampLatest);
		midnightEarlier.set(
			midnightEarlier.get(Calendar.YEAR),
			midnightEarlier.get(Calendar.MONTH),
			midnightEarlier.get(Calendar.DAY_OF_MONTH),
			0,
			0,
			0
		);

		long midnightEarlierTimestamp = midnightEarlier.getTimeInMillis(); // get timetamp of latest day's (earlier) midnight timestamp

		long timestampEqKept = 1000 * 60 * 60 * 24 * daysDatakept; // timestamp equivalent to duration of daysDataKept in milliseconds
		long limitTimestamp = midnightEarlierTimestamp - timestampEqKept; //timestamp equivalent to the oldest data that is to be kept

		/****************************************************************************************************************
		*												TEMPERATURE PROCESSING 											*
		*****************************************************************************************************************/

		MongoCursor<Document> cursor = tempRawCollection.find().sort(descending("timestamp")).iterator();

		int cursorDayIterator = 0;
		int minDay=0, maxDay=0, documentCount = 0;
		float averageDay = 0;
		long cursorTimestamp, dayEndInMillis;
		try {
   			while (cursor.hasNext()) {
   				Document cursorNext = cursor.next();
   				cursorTimestamp = (long) cursorNext.get("timestamp");
    			if (cursorTimestamp >= limitTimestamp) { //cast java Object to long
    				// cursor still in data kept range
    				// do nothing (possibility to count documents here with iterator / process things if you want)
    			} else {
    				// here, we are in the data aggregation range
    				Calendar dayEnd = new GregorianCalendar();
    				dayEnd.clear();
					dayEnd.setTimeInMillis(cursorTimestamp);
					dayEnd.set(
						dayEnd.get(Calendar.YEAR),
						dayEnd.get(Calendar.MONTH),
						dayEnd.get(Calendar.DAY_OF_MONTH),
						0,
						0,
						0
					);
					dayEndInMillis = dayEnd.getTimeInMillis();

					while (((long) cursorNext.get("timestamp") >= dayEndInMillis) && (cursor.hasNext())) {
						//we are in a new day
						if (cursorDayIterator == 0) {
							//if it is the first cursor of the new day, init
							minDay = (int) cursorNext.get("value");
							maxDay = (int) cursorNext.get("value");
							averageDay = (int) cursorNext.get("value");
							cursorNext = cursor.next(); //get the next cursor
							cursorDayIterator++;
						} else {
							//go through the other documents
							if (((int) cursorNext.get("value")) > maxDay) maxDay = ((int) cursorNext.get("value"));
							if (((int) cursorNext.get("value")) < minDay) minDay = ((int) cursorNext.get("value"));
							averageDay += (int) cursorNext.get("value");
							cursorDayIterator++;
							cursorNext = cursor.next();
						}
					}
					// if the code goes here, it means cursorNext.get("timestamp") is lesser or equal to dayEndInMillis
					// thus, it is a new day
					// we can reset for the next loop (this code could be at the beginning of the while(cursor.hasNext) too)
					Document document = new Document("timestamp", dayEndInMillis)
						.append("min", minDay)
						.append("max", maxDay)
						.append("average", averageDay/(cursorDayIterator+1))
						.append("unit", "Degrees Celsius");

					tempAggregatedCollection.insertOne(document);
					cursorDayIterator = 0;
					averageDay = 0;
					minDay =0;
					maxDay = 0;
					documentCount++;
        		}
    		}
		} catch (Exception e) {
			//error processing, not much to do there atm
			Logger.error("AN ERROR OCCURED WHILE PROCESSING TEMPERATURE");
		} finally {
    		cursor.close(); // close the cursor to free the memory
    		Logger.info(documentCount + " aggregation documents have been created");
		}


		/****************************************************************************************************************
		*												HUMIDITY PROCESSING 											*
		*****************************************************************************************************************/

		cursor = humiRawCollection.find().sort(descending("timestamp")).iterator();

		cursorDayIterator = 0;
		minDay=0;
		maxDay=0;
		documentCount = 0;
		averageDay = 0;

		try {
   			while (cursor.hasNext()) {
   				Document cursorNext = cursor.next();
   				cursorTimestamp = (long) cursorNext.get("timestamp");
    			if (cursorTimestamp > limitTimestamp) { //cast java Object to long
    				// cursor still in data kept range
    				// do nothing (possibility to count documents here with iterator / process things maybe)
    			} else {
    				// here, we are in the data aggregation range
    				Calendar dayEnd = new GregorianCalendar();
    				dayEnd.clear();
					dayEnd.setTimeInMillis(cursorTimestamp);
					dayEnd.set(
						dayEnd.get(Calendar.YEAR),
						dayEnd.get(Calendar.MONTH),
						dayEnd.get(Calendar.DAY_OF_MONTH),
						0,
						0,
						0
					);
					dayEndInMillis = dayEnd.getTimeInMillis();

					while (((long) cursorNext.get("timestamp") >= dayEndInMillis) && (cursor.hasNext())) {
						//we are in a new day
						if (cursorDayIterator == 0) {
							//if it is the first cursor of the new day, init
							minDay = (int) cursorNext.get("value");
							maxDay = (int) cursorNext.get("value");
							averageDay = (int) cursorNext.get("value");
							cursorNext = cursor.next(); //get the next cursor
							cursorDayIterator++;
						} else {
							//go through the other documents
							if (((int) cursorNext.get("value")) > maxDay) maxDay = ((int) cursorNext.get("value"));
							if (((int) cursorNext.get("value")) < minDay) minDay = ((int) cursorNext.get("value"));
							averageDay += (int) cursorNext.get("value");
							cursorDayIterator++;
							cursorNext = cursor.next();
						}
					}
					// if the code goes here, it means cursorNext.get("timestamp") is lesser or equal to dayEndInMillis
					// thus, it is a new day
					// we can reset for the next loop (this code could be at the beginning of the while(cursor.hasNext) too)
					Document document = new Document("timestamp", dayEndInMillis)
						.append("min", minDay)
						.append("max", maxDay)
						.append("average", averageDay/(cursorDayIterator+1))
						.append("unit", "Percent");

					humiAggregatedCollection.insertOne(document);
					cursorDayIterator = 0;
					averageDay = 0;
					minDay =0;
					maxDay = 0;
					documentCount++;
        		}
    		}
		} catch (Exception e) {
			//error processing, not much to do there atm
    		cursor.close(); // close the cursor to free the memory
			Logger.error("AN ERROR OCCURED WHILE PROCESSING HUMIDITY");
		} finally {
    		cursor.close(); // close the cursor to free the memory
    		Logger.info(documentCount + " aggregation documents have been created");
		}

		/****************************************************************************************************************
		*												RAIN PROCESSING 												*
		*****************************************************************************************************************/

		cursor = rainRawCollection.find().sort(descending("timestamp")).iterator();

		cursorDayIterator = 0;
		minDay=0;
		maxDay=0;
		documentCount = 0;
		averageDay = 0;

		try {
   			while (cursor.hasNext()) {
   				Document cursorNext = cursor.next();
   				cursorTimestamp = (long) cursorNext.get("timestamp");
    			if (cursorTimestamp > limitTimestamp) { //cast java Object to long
    				// cursor still in data kept range
    				// do nothing (possibility to count documents here with iterator / process things maybe)
    			} else {
    				// here, we are in the data aggregation range
    				Calendar dayEnd = new GregorianCalendar();
    				dayEnd.clear();
					dayEnd.setTimeInMillis(cursorTimestamp);
					dayEnd.set(
						dayEnd.get(Calendar.YEAR),
						dayEnd.get(Calendar.MONTH),
						dayEnd.get(Calendar.DAY_OF_MONTH),
						0,
						0,
						0
					);
					dayEndInMillis = dayEnd.getTimeInMillis();

					while (((long) cursorNext.get("timestamp") >= dayEndInMillis) && (cursor.hasNext())) {
						//we are in a new day
						if (cursorDayIterator == 0) {
							//if it is the first cursor of the new day, init
							minDay = (int) cursorNext.get("value");
							maxDay = (int) cursorNext.get("value");
							averageDay = (int) cursorNext.get("value");
							cursorNext = cursor.next(); //get the next cursor
							cursorDayIterator++;
						} else {
							//go through the other documents
							if (((int) cursorNext.get("value")) > maxDay) maxDay = ((int) cursorNext.get("value"));
							if (((int) cursorNext.get("value")) < minDay) minDay = ((int) cursorNext.get("value"));
							averageDay += (int) cursorNext.get("value");
							cursorDayIterator++;
							cursorNext = cursor.next();
						}
					}
					// if the code goes here, it means cursorNext.get("timestamp") is lesser or equal to dayEndInMillis
					// thus, it is a new day
					// we can reset for the next loop (this code could be at the beginning of the while(cursor.hasNext) too)
					Document document = new Document("timestamp", dayEndInMillis) 
						.append("min", minDay)
						.append("max", maxDay)
						.append("average", averageDay/(cursorDayIterator+1))
						.append("unit", "Millimeter");

					rainAggregatedCollection.insertOne(document);
					cursorDayIterator = 0;
					averageDay = 0;
					minDay =0;
					maxDay = 0;
					documentCount++;
        		}
    		}
		} catch (Exception e) {
			//error processing, not much to do there atm
			Logger.error("AN ERROR OCCURED WHILE PROCESSING RAIN");
		} finally {
    		cursor.close(); // close the cursor to free the memory
    		Logger.info(documentCount + " aggregation documents have been created");
		}



		/****************************************************************************************************************
		*												AIR PROCESSING 													*
		*****************************************************************************************************************/

		cursor = airRawCollection.find().sort(descending("timestamp")).iterator();

		cursorDayIterator = 0;
		minDay=0;
		maxDay=0;
		documentCount = 0;
		averageDay = 0;

		try {
   			while (cursor.hasNext()) {
   				Document cursorNext = cursor.next();
   				cursorTimestamp = (long) cursorNext.get("timestamp");
    			if (cursorTimestamp > limitTimestamp) { //cast java Object to long
    				// cursor still in data kept range
    				// do nothing (possibility to count documents here with iterator / process things maybe)
    			} else {
    				// here, we are in the data aggregation range
    				Calendar dayEnd = new GregorianCalendar();
    				dayEnd.clear();
					dayEnd.setTimeInMillis(cursorTimestamp);
					dayEnd.set(
						dayEnd.get(Calendar.YEAR),
						dayEnd.get(Calendar.MONTH),
						dayEnd.get(Calendar.DAY_OF_MONTH),
						0,
						0,
						0
					);
					dayEndInMillis = dayEnd.getTimeInMillis();

					while (((long) cursorNext.get("timestamp") >= dayEndInMillis) && (cursor.hasNext())) {
						//we are in a new day
						if (cursorDayIterator == 0) {
							//if it is the first cursor of the new day, init
							minDay = (int) cursorNext.get("value");
							maxDay = (int) cursorNext.get("value");
							averageDay = (int) cursorNext.get("value");
							cursorNext = cursor.next(); //get the next cursor
							cursorDayIterator++;
						} else {
							//go through the other documents
							if (((int) cursorNext.get("value")) > maxDay) maxDay = ((int) cursorNext.get("value"));
							if (((int) cursorNext.get("value")) < minDay) minDay = ((int) cursorNext.get("value"));
							averageDay += (int) cursorNext.get("value");
							cursorDayIterator++;
							cursorNext = cursor.next();
						}
					}
					// if the code goes here, it means cursorNext.get("timestamp") is lesser or equal to dayEndInMillis
					// thus, it is a new day
					// we can reset for the next loop (this code could be at the beginning of the while(cursor.hasNext) too)
					Document document = new Document("timestamp", dayEndInMillis) 
						.append("min", minDay)
						.append("max", maxDay)
						.append("average", averageDay/(cursorDayIterator+1))
						.append("unit", "Percent");

					airAggregatedCollection.insertOne(document);
					cursorDayIterator = 0;
					averageDay = 0;
					minDay =0;
					maxDay = 0;
					documentCount++;
        		}
    		}
		} catch (Exception e) {
			//error processing, not much to do there atm
			Logger.error("AN ERROR OCCURED WHILE PROCESSING AIR");
		} finally {
    		cursor.close(); // close the cursor to free the memory
    		Logger.info(documentCount + " aggregation documents have been created");
		}


		/****************************************************************************************************************
		*												WIND SPEED PROCESSING 												*
		*****************************************************************************************************************/

		cursor = windSRawCollection.find().sort(descending("timestamp")).iterator();

		cursorDayIterator = 0;
		minDay = 0;
		maxDay = 0;
		documentCount = 0;
		averageDay = 0;

		try {
   			while (cursor.hasNext()) {
   				Document cursorNext = cursor.next();
   				cursorTimestamp = (long) cursorNext.get("timestamp");
    			if (cursorTimestamp > limitTimestamp) { //cast java Object to long
    				// cursor still in data kept range
    				// do nothing (possibility to count documents here with iterator / process things maybe)
    			} else {
    				// here, we are in the data aggregation range
    				Calendar dayEnd = new GregorianCalendar();
    				dayEnd.clear();
					dayEnd.setTimeInMillis(cursorTimestamp);
					dayEnd.set(
						dayEnd.get(Calendar.YEAR),
						dayEnd.get(Calendar.MONTH),
						dayEnd.get(Calendar.DAY_OF_MONTH),
						0,
						0,
						0
					);
					dayEndInMillis = dayEnd.getTimeInMillis();

					while (((long) cursorNext.get("timestamp") >= dayEndInMillis) && (cursor.hasNext())) {
						//we are in a new day
						if (cursorDayIterator == 0) {
							//if it is the first cursor of the new day, init
							minDay = (int) cursorNext.get("value");
							maxDay = (int) cursorNext.get("value");
							averageDay = (int) cursorNext.get("value");
							cursorNext = cursor.next(); //get the next cursor
							cursorDayIterator++;
						} else {
							//go through the other documents
							if (((int) cursorNext.get("value")) > maxDay) maxDay = ((int) cursorNext.get("value"));
							if (((int) cursorNext.get("value")) < minDay) minDay = ((int) cursorNext.get("value"));
							averageDay += (int) cursorNext.get("value");
							cursorDayIterator++;
							cursorNext = cursor.next();
						}
					}
					// if the code goes here, it means cursorNext.get("timestamp") is lesser or equal to dayEndInMillis
					// thus, it is a new day
					// we can reset for the next loop and agregate the day calculated
					Document document = new Document("timestamp", dayEndInMillis) 
						.append("min", minDay)
						.append("max", maxDay)
						.append("average", averageDay/(cursorDayIterator+1))
						.append("unit", "Knots");

					windSAggregatedCollection.insertOne(document);
					cursorDayIterator = 0;
					averageDay = 0;
					minDay =0;
					maxDay = 0;
					documentCount++;
        		}
    		}
		} catch (Exception e) {
			//error processing, not much to do there atm
			Logger.error("AN ERROR OCCURED WHILE PROCESSING WIND SPEED");
		} finally {
    		cursor.close(); // close the cursor to free the memory
    		Logger.info(documentCount + " aggregation documents have been created");
		}


		/****************************************************************************************************************
		*												WIND DIRECTION PROCESSING 												*
		*****************************************************************************************************************/

		cursor = windDRawCollection.find().sort(descending("timestamp")).iterator();

		cursorDayIterator = 0;
		minDay = 0;
		maxDay = 0;
		documentCount = 0;
		averageDay = 0;

		try {
   			while (cursor.hasNext()) {
   				Document cursorNext = cursor.next();
   				cursorTimestamp = (long) cursorNext.get("timestamp");
    			if (cursorTimestamp > limitTimestamp) { //cast java Object to long
    				// cursor still in data kept range
    				// do nothing (possibility to count documents here with iterator / process things maybe)
    			} else {
    				// here, we are in the data aggregation range
    				Calendar dayEnd = new GregorianCalendar();
    				dayEnd.clear();
					dayEnd.setTimeInMillis(cursorTimestamp);
					dayEnd.set(
						dayEnd.get(Calendar.YEAR),
						dayEnd.get(Calendar.MONTH),
						dayEnd.get(Calendar.DAY_OF_MONTH),
						0,
						0,
						0
					);
					dayEndInMillis = dayEnd.getTimeInMillis();

					while (((long) cursorNext.get("timestamp") >= dayEndInMillis) && (cursor.hasNext())) {
						//we are in a new day
						if (cursorDayIterator == 0) {
							//if it is the first cursor of the new day, init
							minDay = (int) cursorNext.get("value");
							maxDay = (int) cursorNext.get("value");
							averageDay = (int) cursorNext.get("value");
							cursorNext = cursor.next(); //get the next cursor
							cursorDayIterator++;
						} else {
							//go through the other documents
							if (((int) cursorNext.get("value")) > maxDay) maxDay = ((int) cursorNext.get("value"));
							if (((int) cursorNext.get("value")) < minDay) minDay = ((int) cursorNext.get("value"));
							averageDay += (int) cursorNext.get("value");
							cursorDayIterator++;
							cursorNext = cursor.next();
						}
					}
					// if the code goes here, it means cursorNext.get("timestamp") is lesser or equal to dayEndInMillis
					// thus, it is a new day
					// we can reset for the next loop and agregate the day calculated
					Document document = new Document("timestamp", dayEndInMillis) 
						.append("min", minDay)
						.append("max", maxDay)
						.append("average", averageDay/(cursorDayIterator+1))
						.append("unit", "Degrees");

					windDAggregatedCollection.insertOne(document);
					cursorDayIterator = 0;
					averageDay = 0;
					minDay =0;
					maxDay = 0;
					documentCount++;
        		}
    		}
		} catch (Exception e) {
			//error processing, not much to do there atm
			Logger.error("AN ERROR OCCURED WHILE PROCESSING WIND DIRECTION");
		} finally {
    		cursor.close(); // close the cursor to free the memory
    		Logger.info(documentCount + " aggregation documents have been created");
		}		


		/*5 - removal of useless documents*/

		/****************************************************************************************************************
		*												TEMP REMOVAL 													*
		*****************************************************************************************************************/

		MongoCursor<Document> cursorRemoval = tempRawCollection.find(lte("timestamp", limitTimestamp)).sort(descending("timestamp")).iterator();
		int removalCounter = 0;
		try {
			while (cursorRemoval.hasNext()) {
				tempRawCollection.deleteOne(cursorRemoval.next());
				removalCounter++;
			}	
		} catch (Exception e) {
			//error processing, not much to do there atm
			Logger.debug("AN ERROR OCCURED WHILE PROCESSING TEMP REMOVAL");
		} finally {
    		cursorRemoval.close(); // close the cursor to free the memory
    		Logger.info(removalCounter + " documents have been removed");
		}



		/****************************************************************************************************************
		*												HUMI REMOVAL 													*
		*****************************************************************************************************************/		

		cursorRemoval = humiRawCollection.find(lte("timestamp", limitTimestamp)).sort(descending("timestamp")).iterator();
		removalCounter = 0;
		try {
			while (cursorRemoval.hasNext()) {
				humiRawCollection.deleteOne(cursorRemoval.next());
				removalCounter++;
			}	
		} catch (Exception e) {
			//error processing, not much to do there atm
			Logger.debug("AN ERROR OCCURED WHILE PROCESSING HUMI REMOVAL");
		} finally {
    		cursorRemoval.close(); // close the cursor to free the memory
    		Logger.info(removalCounter + " documents have been removed");
		}


		/****************************************************************************************************************
		*												RAIN REMOVAL 													*
		*****************************************************************************************************************/

		cursorRemoval = rainRawCollection.find(lte("timestamp", limitTimestamp)).sort(descending("timestamp")).iterator();
		removalCounter = 0;
		try {
			while (cursorRemoval.hasNext()) {
				rainRawCollection.deleteOne(cursorRemoval.next());
				removalCounter++;
			}	
		} catch (Exception e) {
			//error processing, not much to do there atm
			Logger.debug("AN ERROR OCCURED WHILE PROCESSING RAIN REMOVAL");
		} finally {
    		cursorRemoval.close(); // close the cursor to free the memory
    		Logger.info(removalCounter + " documents have been removed");
		}


		/****************************************************************************************************************
		*												AIR REMOVAL 													*
		*****************************************************************************************************************/

		cursorRemoval = airRawCollection.find(lte("timestamp", limitTimestamp)).sort(descending("timestamp")).iterator();
		removalCounter = 0;
		try {
			while (cursorRemoval.hasNext()) {
				airRawCollection.deleteOne(cursorRemoval.next());
				removalCounter++;
			}	
		} catch (Exception e) {
			//error processing, not much to do there atm
			Logger.debug("AN ERROR OCCURED WHILE PROCESSING AIR REMOVAL");
		} finally {
    		cursorRemoval.close(); // close the cursor to free the memory
    		Logger.info(removalCounter + " documents have been removed");
		}


		/****************************************************************************************************************
		*												WIND SPEED REMOVAL 													*
		*****************************************************************************************************************/		

		cursorRemoval = windSRawCollection.find(lte("timestamp", limitTimestamp)).sort(descending("timestamp")).iterator();
		removalCounter = 0;
		try {
			while (cursorRemoval.hasNext()) {
				windSRawCollection.deleteOne(cursorRemoval.next());
				removalCounter++;
			}	
		} catch (Exception e) {
			//error processing, not much to do there atm
			Logger.debug("AN ERROR OCCURED WHILE PROCESSING WIND SPEED REMOVAL");
		} finally {
    		cursorRemoval.close(); // close the cursor to free the memory
    		Logger.info(removalCounter + " documents have been removed");
		}
	


		/****************************************************************************************************************
		*												WIND DIRECTION REMOVAL 													*
		*****************************************************************************************************************/		

		cursorRemoval = windDRawCollection.find(lte("timestamp", limitTimestamp)).sort(descending("timestamp")).iterator();
		removalCounter = 0;
		try {
			while (cursorRemoval.hasNext()) {
				windDRawCollection.deleteOne(cursorRemoval.next());
				removalCounter++;
			}	
		} catch (Exception e) {
			//error processing, not much to do there atm
			Logger.debug("AN ERROR OCCURED WHILE PROCESSING WIND DIRECTION REMOVAL");
		} finally {
    		cursorRemoval.close(); // close the cursor to free the memory
    		Logger.info(removalCounter + " documents have been removed");
		}
	}
}


