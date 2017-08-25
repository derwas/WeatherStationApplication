package controllers;

//imports for forms and basic play things
import play.data.*;
import play.Logger;
import play.mvc.*;

//import files needed in the project
import views.html.*;
import models.*;

//imports for Java things
import java.util.*;

//imports for date
import java.text.DateFormat;
import java.text.SimpleDateFormat;

//import for injections
import javax.inject.*;

//imports for actors
import akka.actor.*;
import play.libs.F.*;
import play.mvc.WebSocket;
import play.mvc.LegacyWebSocket;

import play.data.FormFactory;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;


/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class Application extends Controller {

    @Inject FormFactory formFactory;
    
    public Result landingPage() {
        Form<Switcher> switchForm = formFactory.form(Switcher.class);
        Switcher switcher = switchForm.bindFromRequest().get();
        String stationName = switcher.getStationName();
        List<List<String>> mongoData = new ArrayList<List<String>>(); // will contain every datas over diff dimensions (Tools.jsonToDataList)
        //get correct data.
        if (stationName == null) mongoData = Tools.jsonToDataList("localhost", 27017, "myDB");
        else { // case where stationName is not null
            // should be modified, done like this because lack of time, see documentation on Switcher class
            Boolean exists = false;
            MongoClient mongoClient = new MongoClient("localhost" , 27017); //opens connection with mongodb server
            MongoCursor<String> mongoCursor = mongoClient.listDatabaseNames().iterator(); // create cursor with database names
            while(mongoCursor.hasNext()) {
                if (mongoCursor.next().equals(stationName)) exists = true; // look if database name exist
            }
            mongoClient.close(); // close client
            if (exists) mongoData = Tools.jsonToDataList("localhost", 27017, stationName); // databse exist, use its data
            else mongoData = Tools.jsonToDataList("localhost", 27017, "myDB"); // else, use default db
            
        }
        /*    INDEX:  
            0 = temperature_raw
            1 = temperature_agr
            2 = humidity_raw
            3 = humidity_agr
            4 = windSpeed_raw
            5 = windSpeed_raw
            6 = windDirection_raw
            7 = windDirection_agr
            8 = rain_raw
            9 = rain_agr
            10 = air_raw
            11 = air_agr*/

        ArrayList<String> mongoDataTempRaw = new ArrayList<String>();
        ArrayList<String> mongoDataTempAgr = new ArrayList<String>();
        ArrayList<String> mongoDataHumiRaw = new ArrayList<String>();
        ArrayList<String> mongoDataHumiAgr = new ArrayList<String>();
        ArrayList<String> mongoDataWindSRaw = new ArrayList<String>();
        ArrayList<String> mongoDataWindSAgr = new ArrayList<String>();
        ArrayList<String> mongoDataWindDRaw = new ArrayList<String>();
        ArrayList<String> mongoDataWindDAgr = new ArrayList<String>();
        ArrayList<String> mongoDataRainRaw = new ArrayList<String>();
        ArrayList<String> mongoDataRainAgr = new ArrayList<String>();
        ArrayList<String> mongoDataAirRaw = new ArrayList<String>();
        ArrayList<String> mongoDataAirAgr = new ArrayList<String>();

        mongoDataTempRaw = Tools.jsonToDataFormat(mongoData, 0);
        mongoDataTempAgr = Tools.jsonToDataFormat(mongoData, 1);
        mongoDataHumiRaw = Tools.jsonToDataFormat(mongoData, 2);
        mongoDataHumiAgr = Tools.jsonToDataFormat(mongoData, 3);
        mongoDataWindSRaw = Tools.jsonToDataFormat(mongoData, 4);
        mongoDataWindSAgr = Tools.jsonToDataFormat(mongoData, 5);
        mongoDataWindDRaw = Tools.jsonToDataFormat(mongoData, 6);
        mongoDataWindDAgr = Tools.jsonToDataFormat(mongoData, 7);
        mongoDataRainRaw = Tools.jsonToDataFormat(mongoData, 8);
        mongoDataRainAgr = Tools.jsonToDataFormat(mongoData, 9);
        mongoDataAirRaw = Tools.jsonToDataFormat(mongoData, 10);
        mongoDataAirAgr = Tools.jsonToDataFormat(mongoData, 11);
        

        ArrayList<Object> allDayTemp = new ArrayList<Object>();
        ArrayList<Object> allDayRain = new ArrayList<Object>();
        ArrayList<Object> allDayHumi = new ArrayList<Object>();
        ArrayList<Object> allDayAir = new ArrayList<Object>();
        ArrayList<Object> allDayWindS = new ArrayList<Object>();
        ArrayList<Object> allDayWindD = new ArrayList<Object>();

        allDayTemp = Tools.getAllDayValues(mongoData, 0);
        allDayHumi = Tools.getAllDayValues(mongoData, 2);
        allDayWindS = Tools.getAllDayValues(mongoData, 4);
        allDayWindD = Tools.getAllDayValues(mongoData, 6);
        allDayRain = Tools.getAllDayValues(mongoData, 8);
        allDayAir = Tools.getAllDayValues(mongoData, 10);

/*        //for testing
        for(int i = 0; i<mongoDataTempAgr.size(); i++){
            Logger.debug(""+mongoDataTempAgr.get(i));
        }*/


        return ok(landing.render(
            mongoDataTempRaw,
            mongoDataTempAgr,
            mongoDataHumiRaw,
            mongoDataHumiAgr,
            mongoDataWindSRaw,
            mongoDataWindSAgr,
            mongoDataWindDRaw,
            mongoDataWindDAgr,
            mongoDataRainRaw,
            mongoDataRainAgr,
            mongoDataAirRaw,
            mongoDataAirAgr,
            allDayTemp,
            allDayHumi,
            allDayWindS,
            allDayWindD,
            allDayRain,
            allDayAir,
            switchForm));
        }

    public Result generator() {
        DataGenerators dg = new DataGenerators();
        dg.fullGeneratorMongoDB("myDB", 500, 24, 7);

        return ok(generator.render());
    }
}
