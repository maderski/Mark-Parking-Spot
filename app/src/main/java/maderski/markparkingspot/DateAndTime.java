package maderski.markparkingspot;

import java.util.Calendar;

/**
 * Created by Jason on 8/9/16.
 */
public class DateAndTime {
    Calendar calendar = Calendar.getInstance();

    //Return current time as a String
    public String getCurrentTime(){
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        if(hour > 12){
            hour -= 12;
        }

        return Integer.toString(hour) + ":" +
                Integer.toString(minute) + getIfAMOrPM();
    }

    //Check to see if it AM or PM and return the correct one
    private String getIfAMOrPM(){
        int amPm = calendar.get(Calendar.AM_PM);

        if(amPm == 0)
            return " AM";
        else
            return " PM";
    }

    //Return the current date as a String
    public String getCurrentDate(){
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);

        return Integer.toString(month) + "-" +
                Integer.toString(day) + "-" +
                Integer.toString(year);
    }

}
