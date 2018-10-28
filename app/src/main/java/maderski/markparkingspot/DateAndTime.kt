package maderski.markparkingspot

import java.util.Calendar

/**
 * Created by Jason on 8/9/16.
 */
class DateAndTime {
    private val calendar = Calendar.getInstance()

    // Return current time as a String
    val currentTime: String
        get() {
            var hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            if (hour > 12) {
                hour -= 12
            } else if (hour == 0) {
                hour = 12
            }

            var minuteStr = Integer.toString(minute)
            if (minute < 10) {
                minuteStr = "0$minuteStr"
            }

            return Integer.toString(hour) + ":" +
                    minuteStr + ifAMOrPM
        }

    //Check to see if it AM or PM and return the correct one
    private val ifAMOrPM: String
        get() {
            val amPm = calendar.get(Calendar.AM_PM)
            return if (amPm == 0) " AM" else " PM"
        }

    // Return the current date as a String
    val currentDate: String
        get() {
            val month = calendar.get(Calendar.MONTH) + 1
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val year = calendar.get(Calendar.YEAR)

            return Integer.toString(month) + "-" +
                    Integer.toString(day) + "-" +
                    Integer.toString(year)
        }

}
