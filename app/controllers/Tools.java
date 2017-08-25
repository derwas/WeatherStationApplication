package controllers;

import java.util.*;

//imports for csv file
import java.io.File;
import java.io.FileNotFoundException;


//imports for date
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import static com.mongodb.client.model.Sorts.*;
import static com.mongodb.client.model.Projections.*;
import play.Logger;

// gives some functionnalities
public class Tools {

    //this method is going to return the data stored in a list
    public static List<List<String>> jsonToDataList(String dbHost, int port, String dbName) {
/*      List[0][x] = temp
        List[1][x] = tempAgr
        List[2][x] = humidity
        List[3][x] = humidityAgr
        List[4][x] = windspeed
        List[5][x] = windspeedAgr
        List[6][x] = winddirection
        List[7][x] = winddirectionAgr
        List[8][x] = rain
        List[9][x] = rainAgr
        List[10][x] = air
        List[11][x] = airAgr*/

        List<List<String>> dataList = new ArrayList<List<String>>();

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


        MongoClient mongoClient = new MongoClient(dbHost , port);
        MongoDatabase database = mongoClient.getDatabase(dbName);

        int i;

        // get database values
        for (i = 0; i < COLLECTIONS.length; i++) { // cycle through every collection
            List<String> list = new ArrayList<String>(); // instanciate a list to write in the second dimension
            dataList.add(list);

            // create a cursor that cycle through the documents
            MongoCursor<Document> cursor = database
                .getCollection(COLLECTIONS[i]) // gets the collection (case sensitive)
                .find() // return everything is to above collection
                .projection(excludeId()) //exclude the id field in the response
                .sort(descending("timestamp")) // latest data will be at the top
                .iterator(); // give the possibility to the cursor to iterate through the result

            try {
                while (cursor.hasNext()) { // loop while there are still documents in the cursor
                    Document cursorNext = cursor.next(); // go to the next document
                    // pair i means raw data
                    if(i%2 == 0) {
                        dataList.get(i).add(cursorNext.get("timestamp").toString());
                        dataList.get(i).add(cursorNext.get("value").toString());
                        dataList.get(i).add(cursorNext.get("unit").toString());
                    } else { //impair i means aggregated data
                        dataList.get(i).add(cursorNext.get("timestamp").toString());
                        dataList.get(i).add(cursorNext.get("min").toString());
                        dataList.get(i).add(cursorNext.get("max").toString());
                        dataList.get(i).add(cursorNext.get("average").toString());
                        dataList.get(i).add(cursorNext.get("unit").toString());
                    }
                }
            } catch (Exception e) {
                Logger.error("An unknown error occured when fetching result from MongoDB");
            } finally {
                cursor.close();
            }
        }
        return dataList;
    }

    //give the function the json data list obtained with jsonToDataList and the index of the data you need (see below)
    //return one dimension array for javascript easier processing
/*    INDEX:  0 = temperature_raw
            1 = temperature_agr
            2 = humidity_raw
            3 = humidity_agr
            4 = windS_raw
            5 = windS_agr
            6 = windD_raw
            7 = windD_agr
            8 = rain_raw
            9 = rain_agr
            10 = air_raw
            11 = air_agr*/
    public static ArrayList<String> jsonToDataFormat(List<List<String>> mongoData, int collectionIndex) {
        ArrayList<String> jsonPushList = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        String timestamp= "", value = "", unit = "", value2 = "", unit2 = "", min = "", max = "", min2 = "", max2 = "", average = "", average2 = "";
        //raw collections
        if (collectionIndex%2 == 0) {
            for (int i=0; i<mongoData.get(collectionIndex).size(); i++) {
                if (i%3 == 0) timestamp = mongoData.get(collectionIndex).get(i);
                if (i%3 == 1) value = mongoData.get(collectionIndex).get(i);
                if (i%3 == 2) {
                    unit = mongoData.get(collectionIndex).get(i);
                    sb.append(".push({timestamp: ")
                    .append(timestamp)
                    .append(", value: ")
                    .append(value)
                    .append(", unit: ")
                    .append("\""+unit+"\"")
                    .append("});");
                    jsonPushList.add(sb.toString());
                    sb.setLength(0); //reset the string builder
                }
            }
        }

        //agr collections
        if (collectionIndex%2 == 1) {
            for (int i=0; i<mongoData.get(collectionIndex).size(); i++) {
                if (i%5 == 0) timestamp = mongoData.get(collectionIndex).get(i);
                if (i%5 == 1) min = mongoData.get(collectionIndex).get(i);
                if (i%5 == 2) max = mongoData.get(collectionIndex).get(i);
                if (i%5 == 3) average = mongoData.get(collectionIndex).get(i);
                if (i%5 == 4) {
                    unit = mongoData.get(collectionIndex).get(i);
                    sb.append(".push({timestamp: ")
                    .append(timestamp)
                    .append(", min: ")
                    .append(min)
                    .append(", max: ")
                    .append(max)
                    .append(", average: ")
                    .append(average)
                    .append("});");
                    jsonPushList.add(sb.toString());
                    sb.setLength(0); //reset the string builder
                }
            }
        }

        return jsonPushList;
    }




    //takes the list of list<String> generated with jsonToDataList, and the index of the collection wished
/*  INDEX:  0 = temperature_raw
            2 = humidity_raw
            4 = wind_raw
            6 = rain_raw
            8 = air_raw*/
    public static ArrayList<Object> getAllDayValues(List<List<String>> mongoData, int collectionIndex) {
        ArrayList<Object> stats = new ArrayList<Object>();
        long lastTimestamp = 0,
            dayLimitTimestamp = 0,
            nextTimestamp = 0,
            minTime = 0,
            maxTime = 0,
            currentTime = 0,
            minTimeWS = 0,
            maxTimeWS = 0,
            currentTimeWS = 0,
            minTimeWD = 0,
            maxTimeWD = 0,
            currentTimeWD = 0;

        int i = 0,
            min = 0,
            max = 0,
            current = 0,
            currentWS = 0,
            minWS = 0,
            maxWS = 0,
            currentWD = 0,
            minWD = 0,
            maxWD = 0;

        float average = 0,
            averageWS = 0,
            averageWD = 0;

        lastTimestamp = Long.parseLong(mongoData.get(collectionIndex).get(0));
        Calendar dayLimit = Calendar.getInstance();
        dayLimit.setTimeInMillis(lastTimestamp); // set time to the latest timestamp
        dayLimit.set( // set time at the beginning of the same day
            dayLimit.get(Calendar.YEAR),
            dayLimit.get(Calendar.MONTH),
            dayLimit.get(Calendar.DAY_OF_MONTH),
            0, //sets hours to 0
            0, //set minutes to 0
            0 // sets seconds to 0
        ); 

        if (collectionIndex%2 == 0) { //raws
            current = Integer.parseInt(mongoData.get(collectionIndex).get(1));
            currentTime = lastTimestamp;
            dayLimitTimestamp = dayLimit.getTimeInMillis(); // get time in milliseconds
            nextTimestamp = lastTimestamp; // for while loop and var name comprehension
            while (nextTimestamp >= dayLimitTimestamp) {
                if (i == 0) { // cursor on first entry (init timestamps)
                    minTime = maxTime =  Long.parseLong(mongoData.get(collectionIndex).get(i));
                    nextTimestamp = Long.parseLong(mongoData.get(collectionIndex).get(i+3));
                } else if (i == 1) { // cursor on second entry (init values)
                    min = max = Integer.parseInt(mongoData.get(collectionIndex).get(i)); 
                    average += Integer.parseInt(mongoData.get(collectionIndex).get(i));
                } else if (i%3 == 1) { //cursor on every seconds entries (except the first second)
                    average += Integer.parseInt(mongoData.get(collectionIndex).get(i)); // add value to average
                    nextTimestamp = Long.parseLong(mongoData.get(collectionIndex).get(i+2));
                    if (Integer.parseInt(mongoData.get(collectionIndex).get(i)) > max) { //case where value gt max
                        max = Integer.parseInt(mongoData.get(collectionIndex).get(i)); // replace max with new value
                        maxTime = Long.parseLong(mongoData.get(collectionIndex).get(i-1)); // get time associated with this value
                    } else if (Integer.parseInt(mongoData.get(collectionIndex).get(i)) < min) { // case where value lt min
                        min = Integer.parseInt(mongoData.get(collectionIndex).get(i)); // replace min with new value
                        minTime = Long.parseLong(mongoData.get(collectionIndex).get(i-1)); // get time associated with this value
                    }
                }
                i++;
            }
            average /= ((i+1)/3); //divides average with the number of iterations divided by the amount of "loop entries"
                                // because when i is incremented, it only went through one entry
                                // while a full data is composed of 3 entries here
                                // thus i is iterated three times as much as there are data
            stats.add(min);
            stats.add(minTime);
            stats.add(max);
            stats.add(maxTime);
            stats.add(average);
            stats.add(current);
            stats.add(currentTime);
        }
        
        return stats;
    }


}