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


public class Application extends Controller {

    @Inject FormFactory formFactory;

    public Result landingPage() {
        Form<Switcher> switchForm = formFactory.form(Switcher.class);
        Switcher switcher = switchForm.bindFromRequest().get();
        String stationName = switcher.getStationName();
        List<List<String>> mongoData = new ArrayList<List<String>>();
        
        ArrayList<String> weatherCond = new ArrayList<String>(); 
        if (stationName == null)
		{
			 mongoData = Tools.jsonToDataList("localhost", 27017, "weathergalway");
			weatherCond = Tools.getWeatherCondition("localhost", 27017, "weathergalway");
		}
        else { 
           
            Boolean exists = false;
            MongoClient mongoClient = new MongoClient("localhost" , 27017); 
            MongoCursor<String> mongoCursor = mongoClient.listDatabaseNames().iterator(); 
            while(mongoCursor.hasNext()) {                if (mongoCursor.next().equals(stationName)) exists = true; 
            }
            mongoClient.close(); 
            if (exists){
			 mongoData = Tools.jsonToDataList("localhost", 27017, stationName); 
			weatherCond = Tools.getWeatherCondition("localhost", 27017, stationName);
		}
            else 
		{
			mongoData = Tools.jsonToDataList("localhost", 27017, "weathergalway"); 
                        weatherCond = Tools.getWeatherCondition("localhost", 27017, "weathergalway");

		}
        }

        ArrayList<String> mongoDataTempRaw = new ArrayList<String>();
        ArrayList<String> mongoDataHumiRaw = new ArrayList<String>();
        ArrayList<String> mongoDataWindSRaw = new ArrayList<String>();
        ArrayList<String> mongoDataWindDRaw = new ArrayList<String>();


        mongoDataTempRaw = Tools.jsonToDataFormat(mongoData, 0);
        mongoDataHumiRaw = Tools.jsonToDataFormat(mongoData, 2);
        mongoDataWindSRaw = Tools.jsonToDataFormat(mongoData, 4);
        mongoDataWindDRaw = Tools.jsonToDataFormat(mongoData, 6);


        ArrayList<Object> allDayTemp = new ArrayList<Object>();
        ArrayList<Object> allDayHumi = new ArrayList<Object>();
        ArrayList<Object> allDayWindS = new ArrayList<Object>();
        ArrayList<Object> allDayWindD = new ArrayList<Object>();

        allDayTemp = Tools.getAllDayValues(mongoData, 0);
        allDayHumi = Tools.getAllDayValue(mongoData, 2);
        allDayWindS = Tools.getAllDayValuee(mongoData, 4);
        allDayWindD = Tools.getAllDayValueee(mongoData, 6);


        return ok(landing.render(
            weatherCond,
            mongoDataTempRaw,
            mongoDataHumiRaw,
            mongoDataWindSRaw,
            mongoDataWindDRaw,
            allDayTemp,
            allDayHumi,
            allDayWindS,
            allDayWindD,
            switchForm));
        }

    public Result generator() {
        DataGenerators dg = new DataGenerators();
        dg.fullGeneratorMongoDB("weathergalway", 500, 24, 7);

        return ok(generator.render());
    }
}

