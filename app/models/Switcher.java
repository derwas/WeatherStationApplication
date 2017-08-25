package models;

import java.util.*;
import play.data.validation.Constraints.*;
import play.data.validation.*;


public class Switcher {

	protected String stationName;

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
        this.stationName = stationName;
    }

}